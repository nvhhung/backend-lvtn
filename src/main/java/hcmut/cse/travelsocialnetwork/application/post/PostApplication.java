package hcmut.cse.travelsocialnetwork.application.post;

import hcmut.cse.travelsocialnetwork.application.follow.FollowApplication;
import hcmut.cse.travelsocialnetwork.application.media.IMediaApplication;
import hcmut.cse.travelsocialnetwork.application.user.HelperUser;
import hcmut.cse.travelsocialnetwork.command.follow.CommandFollow;
import hcmut.cse.travelsocialnetwork.command.media.CommandMedia;
import hcmut.cse.travelsocialnetwork.command.post.CommandPost;
import hcmut.cse.travelsocialnetwork.model.Follow;
import hcmut.cse.travelsocialnetwork.model.Media;
import hcmut.cse.travelsocialnetwork.model.Post;
import hcmut.cse.travelsocialnetwork.repository.media.IMediaRepository;
import hcmut.cse.travelsocialnetwork.repository.post.IPostRepository;
import hcmut.cse.travelsocialnetwork.service.redis.PostRedis;
import hcmut.cse.travelsocialnetwork.service.redis.UserRedis;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public PostApplication(HelperUser helperUser,
            IPostRepository postRepository,
            UserRedis userRedis,
            PostRedis postRedis,
                           IMediaApplication mediaApplication,
                           FollowApplication followApplication) {
        this.helperUser = helperUser;
        this.postRepository = postRepository;
        this.userRedis = userRedis;
        this.postRedis = postRedis;
        this.mediaApplication = mediaApplication;
        this.followApplication = followApplication;
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

        Optional.ofNullable(commandPost.getMediaList()).ifPresent(medias -> medias.forEach(media -> {
            var commandMedia = CommandMedia.builder()
                    .postId(post.get_id().toString())
                    .link(media.getLink())
                    .type(media.getType())
                    .build();
            mediaApplication.add(commandMedia);
        }));
        post.setMediaList(commandPost.getMediaList());
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

        // update media
//        Optional.ofNullable(commandPost.getMediaList()).ifPresent(mediaList -> {
//            var resultDelete = mediaRepository.deleteMany(new Document("postId", post.get_id().toString()));
//            post.setMediaList(new ArrayList<>());
//            log.info("delete old media of post result = {}", resultDelete);
//            mediaList.forEach(media -> {
//                media.setPostId(post.get_id().toString());
//                var mediaNew = mediaRepository.add(media);
//                if (mediaNew.isEmpty()) {
//                    log.info("save media have link {} error", media.getLink());
//                    return;
//                }
//                post.getMediaList().add(mediaNew.get());
//            });
//        });

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
        return postRepository.delete(commandPost.getId());
    }

    @Override
    public Optional<List<Post>> loadPost(CommandPost commandPost) throws Exception {
        var query = new Document();
        var sort = new Document(Constant.FIELD.LAST_UPDATE_TIME, -1);
        var postList = postRepository.search(query, sort, commandPost.getPage(), commandPost.getSize());
        postList.ifPresentOrElse(posts -> log.info("user {} load post have size {}", commandPost.getUserId(), posts.size()),
                () -> log.info("user {} load no post", commandPost.getUserId()));
        return postList;
    }

    @Override
    public Optional<List<Post>> loadRelationPost(CommandPost commandPost) throws Exception {
        var postList = new ArrayList<Post>();
        var sort = new Document(Constant.FIELD.LAST_UPDATE_TIME, -1);
        // get list user follow
        var commandFollow = CommandFollow.builder()
            .userId(commandPost.getUserId())
            .page(0)
            .size(1000)
            .build();

        var userFollowList = followApplication.getFollowUser(commandFollow);
        userFollowList.ifPresentOrElse(follows -> follows.forEach(follow -> {
            var posts = postRepository.search(new Document("userId", follow.getUserIdTarget()), sort, commandPost.getPage(), commandPost.getSize());
            posts.ifPresent(post -> {
            });
        }), () -> {
            var posts = postRepository.search(new Document(), sort, commandPost.getPage(), commandPost.getSize());
            posts.ifPresent(postList::addAll);
        });

        return Optional.of(postList);
    }

    @Override
    public Optional<List<Post>> searchPost(CommandPost commandPost) throws Exception {
        return Optional.empty();
    }

}
