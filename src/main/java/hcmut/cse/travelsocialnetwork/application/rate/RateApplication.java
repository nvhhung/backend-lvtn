package hcmut.cse.travelsocialnetwork.application.rate;

import hcmut.cse.travelsocialnetwork.command.rate.CommandRate;
import hcmut.cse.travelsocialnetwork.model.Rate;
import hcmut.cse.travelsocialnetwork.repository.rate.IRateRepository;
import hcmut.cse.travelsocialnetwork.service.redis.PostRedis;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 9/20/22 Tuesday
 **/
@Component
public class RateApplication implements IRateApplication{

    private static final Logger log = LogManager.getLogger(RateApplication.class);
    IRateRepository rateRepository;
    PostRedis postRedis;

    public RateApplication(IRateRepository rateRepository,
                           PostRedis postRedis) {
        this.rateRepository = rateRepository;
        this.postRedis = postRedis;
    }

    @Override
    public Optional<Rate> mark(CommandRate commandRate) throws Exception {
        var rateNew = Rate.builder()
                .userId(commandRate.getUserId())
                .postId(commandRate.getPostId())
                .point(commandRate.getPoint())
                .build();
        var rateAdd = rateRepository.add(rateNew);
        if (rateAdd.isEmpty()) {
            log.warn(String.format("%s rate post %s fail", commandRate.getUserId(), commandRate.getPostId()));
            throw new CustomException(Constant.ERROR_MSG.RATE_FAIL);
        }
        // todo: push notification to owner of post
        // increase point of post
        var post = postRedis.getPost(commandRate.getPostId());
        post.setPoint(post.getPoint() + Constant.POINTS.ONE_LIKE_POST);
        postRedis.updatePostRedisDB(commandRate.getPostId(), post);
        return rateAdd;
    }
}
