package hcmut.cse.travelsocialnetwork.application.post;

import com.google.api.client.util.Strings;
import hcmut.cse.travelsocialnetwork.application.follow.FollowApplication;
import hcmut.cse.travelsocialnetwork.application.media.IMediaApplication;
import hcmut.cse.travelsocialnetwork.application.search.ISearchApplication;
import hcmut.cse.travelsocialnetwork.application.user.HelperUser;
import hcmut.cse.travelsocialnetwork.command.follow.CommandFollow;
import hcmut.cse.travelsocialnetwork.command.media.CommandMedia;
import hcmut.cse.travelsocialnetwork.command.post.CommandPost;
import hcmut.cse.travelsocialnetwork.model.Media;
import hcmut.cse.travelsocialnetwork.model.Post;
import hcmut.cse.travelsocialnetwork.repository.post.IPostRepository;
import hcmut.cse.travelsocialnetwork.service.elasticsearch.ParamElasticsearchObj;
import hcmut.cse.travelsocialnetwork.service.redis.PostRedis;
import hcmut.cse.travelsocialnetwork.service.redis.UserRedis;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : hung.nguyen23
 * @since : 8/30/22 Tuesday
 **/
@Component
public class PostApplication implements IPostApplication{
    private static final Logger log = LogManager.getLogger(PostApplication.class);

    HelperUser helperUser;
    IPostRepository postRepository;
    UserRedis userRedis;
    PostRedis postRedis;
    FollowApplication followApplication;
    IMediaApplication mediaApplication;
    ISearchApplication searchApplication;

    public PostApplication(HelperUser helperUser, IPostRepository postRepository, UserRedis userRedis,
            PostRedis postRedis, IMediaApplication mediaApplication, FollowApplication followApplication,
                           ISearchApplication searchApplication) {
        this.helperUser = helperUser;
        this.postRepository = postRepository;
        this.userRedis = userRedis;
        this.postRedis = postRedis;
        this.mediaApplication = mediaApplication;
        this.followApplication = followApplication;
        this.searchApplication = searchApplication;
    }


    @Override
    public Optional<Post> createPost(CommandPost commandPost) throws Exception {
        var user = userRedis.getUser(commandPost.getUserId());
        var post = Post.builder()
                .userId(commandPost.getUserId())
                .title(commandPost.getTitle())
                .content(commandPost.getContent())
                .type(commandPost.getType())
                .destination(commandPost.getDestination())
                .commentSize(0)
                .likeSize(0)
                .rateSize(0)
                .status(commandPost.getStatus())
                .point(0)
                .build();
        var postAdd = postRepository.add(post);
        if (postAdd.isEmpty()) {
            log.info(String.format("create post by user = %s fail", user.getFullName()));
            throw new CustomException(Constant.ERROR_MSG.POST_FAIL);
        }

        Optional.ofNullable(commandPost.getMediaList()).ifPresent(medias -> {
            post.setMediaList(new ArrayList<>());
            medias.forEach(media -> {
                var commandMedia = CommandMedia.builder()
                        .postId(post.get_id().toString())
                        .link(media.getLink())
                        .type(media.getType())
                        .build();
                var mediaAdd = mediaApplication.add(commandMedia);
                mediaAdd.ifPresent(mediaAddSuccess -> post.getMediaList().add(mediaAddSuccess));
            });
        });
        postRedis.updatePost(postAdd.get().get_id().toString(), postAdd.get());

        // increase point for owner user post
        user.setExperiencePoint(user.getExperiencePoint() + Constant.POINTS.ONE_RATE_USER);
        userRedis.updateUserRedisDB(user.get_id().toString(), user);
        return postAdd;
    }

    @Override
    public Optional<Post> updatePost(CommandPost commandPost) throws Exception {
        var postTemp = postRepository.getById(commandPost.getId());
        if (postTemp.isEmpty()) {
            log.error(String.format("not found post have id = %s", commandPost.getId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_POST);
        }
        var userPost = userRedis.getUser(commandPost.getUserId());
        var post = postTemp.get();
        Optional.ofNullable(commandPost.getTitle()).ifPresent(post::setTitle);
        Optional.ofNullable(commandPost.getContent()).ifPresent(post::setContent);
        Optional.ofNullable(commandPost.getStatus()).ifPresent(post::setStatus);
        Optional.ofNullable(commandPost.getType()).ifPresent(post::setType);
        Optional.ofNullable(commandPost.getDestination()).ifPresent(post::setDestination);

        var postUpdate = postRepository.update(post.get_id().toString(), post);
        if (postUpdate.isEmpty()) {
            log.info(String.format("update post have id = %s fail",post.get_id()));
            throw new CustomException(Constant.ERROR_MSG.UPDATE_POST_FAIL);
        }

        Optional.ofNullable(commandPost.getMediaList()).ifPresent(mediaList -> {
            var linkList = mediaList.stream()
                    .map(Media::getLink)
                    .collect(Collectors.toList());
            var commandMediaCurrent = CommandMedia.builder()
                    .page(1)
                    .size(1000)
                    .postId(commandPost.getId())
                    .build();
            var mediaListCurrentOptional = mediaApplication.loadByPostId(commandMediaCurrent);
            mediaListCurrentOptional.ifPresentOrElse(mediaListCurrent -> {
                // check different in request
                var mediaDifferent = mediaList.stream()
                                .filter(media -> !linkList.contains(media.getLink()))
                                .collect(Collectors.toList());
                if (!mediaDifferent.isEmpty()) {
                    mediaDifferent.forEach(media -> {
                        mediaApplication.delete(media.get_id().toString());
                        mediaListCurrent.remove(media);
                    });
                }

                var linkNew = linkList.stream()
                        .filter(link -> !mediaListCurrent.stream().map(Media::getLink).collect(Collectors.toList()).contains(link))
                        .collect(Collectors.toList());
                mediaList.stream().filter(media -> linkNew.contains(media.getLink()))
                        .forEach(media -> {
                            var commandMedia = CommandMedia.builder()
                                    .link(media.getLink())
                                    .type(media.getType())
                                    .postId(commandPost.getId())
                                    .build();
                            mediaApplication.add(commandMedia);
                        });
            }, () -> {
                // add all
                mediaList.forEach(media -> {
                    var commandMediaAddNew = CommandMedia.builder()
                            .type(media.getType())
                            .link(media.getLink())
                            .build();
                    mediaApplication.add(commandMediaAddNew);
                });
            });
        });

        log.info(String.format("update post have id = %s by user %s successful", post.get_id(), userPost.getFullName()));
        postRedis.updatePost(postUpdate.get().get_id().toString(), postUpdate.get());
        return postUpdate;
    }

    @Override
    public Optional<Post> getPost(CommandPost commandPost) throws Exception {
        var postTemp = postRedis.getPost(commandPost.getId());
        if (postTemp == null) {
            log.error(String.format("not found post have id = %s", commandPost.getId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_POST);
        }
        return Optional.of(postTemp);
    }

    @Override
    public Optional<Boolean> deletePost(CommandPost commandPost) throws Exception {
        var postTemp = postRepository.getById(commandPost.getId());
        if (postTemp.isEmpty()) {
            log.error(String.format("not found post have id = %s", commandPost.getId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_POST);
        }
        postRedis.deletePost(commandPost.getId());
        return Optional.of(true);
    }

    @Override
    public Optional<List<Post>> loadAllPost(CommandPost commandPost) throws Exception {
        var query = new Document();
        var sort = new Document(Constant.FIELD_QUERY.LAST_UPDATE_TIME, -1);
        var postList = postRepository.search(query, sort, commandPost.getPage(), commandPost.getSize());
        postList.ifPresentOrElse(posts -> {
            log.info("user {} load post have size {}", commandPost.getUserId(), posts.size());
            posts.forEach(post -> post.setMediaList(mediaApplication.loadByPostId(CommandMedia.builder()
                    .postId(post.get_id().toString())
                    .page(1)
                    .size(1000)
                    .build()).orElse(new ArrayList<>())));
            }, () -> log.info("user {} load no post", commandPost.getUserId()));
        return postList;
    }

    @Override
    public Optional<List<Post>> loadRelationPost(CommandPost commandPost) throws Exception {
        var postListResult = new ArrayList<Post>();
        var sort = new Document(Constant.FIELD_QUERY.LAST_UPDATE_TIME, -1);
        // get list user follow
        var commandFollow = CommandFollow.builder()
            .userId(commandPost.getUserId())
            .page(0)
            .size(1000)
            .build();

        var userFollowList = followApplication.getFollowUser(commandFollow);
        userFollowList.ifPresentOrElse(follows -> follows.forEach(follow -> {
            var postFollow = postRepository.search(new Document("userId", follow.getUserIdTarget()), sort, commandPost.getPage(), commandPost.getSize());
            postFollow.ifPresent(postFollowUser -> {
                postFollowUser.forEach(post -> {
                    post.setMediaList(mediaApplication.loadByPostId(CommandMedia.builder()
                            .postId(post.get_id().toString())
                            .page(1)
                            .size(1000)
                            .build()).orElse(new ArrayList<>()));
                    postListResult.add(post);
                });
            });
        }), () -> {
            var posts = postRepository.search(new Document(), sort, commandPost.getPage(), commandPost.getSize());
            posts.ifPresent(postListResult::addAll);
        });

        return Optional.of(postListResult);
    }

    @Override
    public Optional<List<Post>> searchPost(CommandPost commandPost) throws Exception {
        var postListResult = new ArrayList<Post>();
        var queryModel = new HashMap<String, Object>();

        Optional.ofNullable(commandPost.getKeyword()).ifPresent(keyword -> queryModel.put("keyword", commandPost.getKeyword()));
        if (!Strings.isNullOrEmpty(commandPost.getType())) {
            queryModel.put("type", commandPost.getType());
        }
        if (!Strings.isNullOrEmpty(commandPost.getDestination())) {
            queryModel.put("destination", commandPost.getDestination());
        }

        var params = ParamElasticsearchObj.builder()
                .templateCfgKey(Constant.GLOBAL_CONFIG.QUERY_ES_POST)
                .clazz(Post.class)
                .index("post")
                .from((commandPost.getPage() - 1) * commandPost.getSize())
                .size(commandPost.getSize())
                .minScore(0)
                .queryModel(queryModel)
            .build();
        var hitPostList = searchApplication.searchES(params);
        if (hitPostList == null) {
            log.warn("post from elasticsearch null");
            throw new CustomException("post from elasticsearch null");
        }

        hitPostList.forEach(hitPost -> postListResult.add(postRedis.getPost(hitPost.id())));
        return Optional.of(postListResult);
    }

    @Override
    public Optional<List<Post>> loadByUserId(CommandPost commandPost) throws Exception {
        var query = new Document(Constant.FIELD_QUERY.USER_ID,  commandPost.getUserId());
        var sort = new Document(Constant.FIELD_QUERY.LAST_UPDATE_TIME, -1);
        var postList = postRepository.search(query, sort, commandPost.getPage(), commandPost.getSize());
        postList.ifPresentOrElse(posts -> {
            log.info("user {} load post have size {}", commandPost.getUserId(), posts.size());
            posts.forEach(post -> post.setMediaList(mediaApplication.loadByPostId(CommandMedia.builder()
                    .postId(post.get_id().toString())
                    .page(1)
                    .size(1000)
                    .build()).orElse(new ArrayList<>())));
        }, () -> log.info("user {} load no post", commandPost.getUserId()));
        return postList;
    }
}
