package hcmut.cse.travelsocialnetwork.application.search;

import hcmut.cse.travelsocialnetwork.model.Post;
import hcmut.cse.travelsocialnetwork.model.User;
import hcmut.cse.travelsocialnetwork.repository.post.IPostRepository;
import hcmut.cse.travelsocialnetwork.repository.user.IUserRepository;
import hcmut.cse.travelsocialnetwork.service.elasticsearch.ElasticsearchClient;
import hcmut.cse.travelsocialnetwork.service.elasticsearch.ParamElasticsearchObj;
import hcmut.cse.travelsocialnetwork.service.vertx.rest.RestfulVerticle;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

import static hcmut.cse.travelsocialnetwork.utils.Constant.TIME.MILLISECOND_OF_THREE_MINUTE;
import static hcmut.cse.travelsocialnetwork.utils.Constant.TIME.MILLISECOND_OF_TWO_MINUTE;

/**
 * @author : hung.nguyen23
 * @since : 9/27/22 Tuesday
 **/
@Component
public class SearchApplication implements ISearchApplication {
    private static final Logger log = LogManager.getLogger(SearchApplication.class);
    private final ElasticsearchClient elasticsearchClient;
    private final RestfulVerticle restfulVerticle;
    private final IUserRepository userRepository;
    private final IPostRepository postRepository;


    public SearchApplication(ElasticsearchClient elasticsearchClient,
                             RestfulVerticle restfulVerticle,
                             IUserRepository userRepository,
                             IPostRepository postRepository) {
        this.elasticsearchClient = elasticsearchClient;
        this.restfulVerticle = restfulVerticle;
        this.userRepository = userRepository;
        this.postRepository = postRepository;

        syncUser();
        syncPost();
    }

    private void syncUser() {
        restfulVerticle.getVertx().setPeriodic(MILLISECOND_OF_THREE_MINUTE, along -> {
            var userListAll = userRepository.search(new Document(), new Document("lastUpdateTime", -1), 0, 1000);
            if (userListAll.isEmpty()) {
                log.warn("no find all user");
                return;
            }
            var paramElasticsearchObj = ParamElasticsearchObj.builder()
                    .index("user")
                    .clazz(User.class)
                    .upsert(true)
                    .build();

            var atomicInt = new AtomicInteger(0);
            restfulVerticle.getVertx().setPeriodic(200L, alongElasticsearch -> {
                if (atomicInt.get() == userListAll.get().size()) {
                    log.info("upsert user done: " + userListAll.get().size());
                    restfulVerticle.getVertx().cancelTimer(alongElasticsearch);
                    return;
                }

                var userSync = userListAll.get().get(atomicInt.getAndIncrement());
                paramElasticsearchObj.setJBody(buildJBodyUser(userSync));
                paramElasticsearchObj.setId(userSync.get_id().toString());
                elasticsearchClient.update(paramElasticsearchObj, paramElasticsearchObj.getClazz(), result -> {
                    log.info("update user id {} to elasticsearch result {}", userSync.get_id(),  result);
                });
            });
        });
    }

    private void syncPost() {
        restfulVerticle.getVertx().setPeriodic(MILLISECOND_OF_TWO_MINUTE, along -> {
            var postList = postRepository.search(new Document(), new Document("lastUpdateTime", -1), 1, 5000);
            if (postList.isEmpty()) {
                log.warn("no find all post");
                return;
            }
            var paramElasticsearchObj = ParamElasticsearchObj.builder()
                    .index("post")
                    .clazz(Post.class)
                    .upsert(true)
                    .build();

            var atomicInt = new AtomicInteger(0);
            restfulVerticle.getVertx().setPeriodic(200L, alongElasticsearch -> {
                if (atomicInt.get() == postList.get().size()) {
                    log.info("upsert elasticsearch done: " + postList.get().size());
                    restfulVerticle.getVertx().cancelTimer(alongElasticsearch);
                    return;
                }

                var postSync = postList.get().get(atomicInt.getAndIncrement());
                paramElasticsearchObj.setJBody(buildJBodyPost(postSync));
                paramElasticsearchObj.setId(postSync.get_id().toString());
                elasticsearchClient.update(paramElasticsearchObj, paramElasticsearchObj.getClazz(), result -> {
                    log.info("update post id {} to elasticsearch result {}", postSync.get_id(),  result);
                });
            });
        });
    }

    private JsonObject buildJBodyUser(User user) {
        var jo = new JsonObject();
        jo.put("fullName", user.getFullName());
        jo.put("phone", user.getPhone());
        jo.put("username", user.getUsername());
        jo.put("email", user.getEmail());
        return jo;
    }

    private JsonObject buildJBodyPost(Post post) {
        var jo = new JsonObject();
        jo.put("title", post.getTitle());
        jo.put("content", post.getContent());
        jo.put("destination", post.getDestination());
        jo.put("type", post.getType());
        return jo;
    }
}
