package hcmut.cse.travelsocialnetwork.application.rate;

import hcmut.cse.travelsocialnetwork.command.rate.CommandRate;
import hcmut.cse.travelsocialnetwork.model.Rate;
import hcmut.cse.travelsocialnetwork.repository.rate.IRateRepository;
import hcmut.cse.travelsocialnetwork.service.redis.PostRedis;
import hcmut.cse.travelsocialnetwork.service.redis.UserRedis;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import hcmut.cse.travelsocialnetwork.utils.enums.FactorialPost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
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
    UserRedis userRedis;

    public RateApplication(IRateRepository rateRepository,
                           PostRedis postRedis,
                           UserRedis userRedis) {
        this.rateRepository = rateRepository;
        this.postRedis = postRedis;
        this.userRedis = userRedis;
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
        postRedis.increaseFactorial(commandRate.getPostId(), FactorialPost.RATE);
        postRedis.increasePoints(commandRate.getPostId(), commandRate.getPoint());
        userRedis.increasePoints(commandRate.getUserId(), Constant.POINTS.ONE_RATE_USER);
        //todo: push notification to owner of post

        return rateAdd;
    }

    @Override
    public Optional<Boolean> unmark(CommandRate commandRate) throws Exception {
        var query = new Document("userId", commandRate.getUserId()).append("postId", commandRate.getPostId());
        var rate = rateRepository.get(query);
        if (rate.isEmpty()) {
            log.warn(String.format("%s rate post %s fail", commandRate.getUserId(), commandRate.getPostId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_RATE);
        }
        postRedis.decreaseFactorial(commandRate.getPostId(), FactorialPost.RATE);
        postRedis.decreasePoints(commandRate.getPostId(),rate.get().getPoint());
        userRedis.decreasePoints(commandRate.getUserId(), Constant.POINTS.ONE_RATE_USER);
        return rateRepository.delete(rate.get().get_id().toString());
    }
}
