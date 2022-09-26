package hcmut.cse.travelsocialnetwork.application.search;

import hcmut.cse.travelsocialnetwork.application.user.UserApplication;
import hcmut.cse.travelsocialnetwork.model.User;
import hcmut.cse.travelsocialnetwork.repository.post.IPostRepository;
import hcmut.cse.travelsocialnetwork.repository.user.IUserRepository;
import hcmut.cse.travelsocialnetwork.service.elasticsearch.ElasticsearchClient;
import hcmut.cse.travelsocialnetwork.service.elasticsearch.ParamElasticsearchObj;
import hcmut.cse.travelsocialnetwork.service.vertx.rest.RestfulVerticle;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : hung.nguyen23
 * @since : 9/27/22 Tuesday
 **/
@Component
public class SearchApplication implements ISearchApplication {
    private static final Logger log = LogManager.getLogger(UserApplication.class);
    private ElasticsearchClient elasticsearchClient;
    private RestfulVerticle restfulVerticle;
    private IUserRepository userRepository;
    private IPostRepository postRepository;


    public SearchApplication(ElasticsearchClient elasticsearchClient,
                             RestfulVerticle restfulVerticle,
                             IUserRepository userRepository,
                             IPostRepository postRepository) {
        this.elasticsearchClient = elasticsearchClient;
        this.restfulVerticle = restfulVerticle;
        this.userRepository = userRepository;
        this.postRepository = postRepository;

        syncUser();
        syncPos();
    }

    private void syncUser() {
        restfulVerticle.getVertx().setPeriodic(Constant.TIME.MILLISECOND_OF_FOUR_MINUTE, along -> {
            var userListAll = userRepository.search(new Document(), new Document("lastUpdateTime", -1), 1, 0);
            if (userListAll.isEmpty()) {
                log.warn("no find all user");
                return;
            }
            var param = ParamElasticsearchObj.builder()
                    .index("user")
                    .clazz(User.class)
                    .upsert(true)
                    .build();

            var atomicInt = new AtomicInteger();
            restfulVerticle.getVertx().setPeriodic(200L, alongElasticsearch -> {
                if (atomicInt.get() == userListAll.get().size()) {
                    log.info("upsert user done: " + userListAll.get().size());
                    restfulVerticle.getVertx().cancelTimer(alongElasticsearch);
                    return;
                }

                var userSync = userListAll.get().get(atomicInt.getAndIncrement());

            });
        });
    }

    private void syncPos() {

    }

    private void buildJBodyIndex() {

    }
}
