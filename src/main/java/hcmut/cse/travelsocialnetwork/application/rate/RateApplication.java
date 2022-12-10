package hcmut.cse.travelsocialnetwork.application.rate;

import hcmut.cse.travelsocialnetwork.application.notification.INotificationApplication;
import hcmut.cse.travelsocialnetwork.command.notification.CommandNotification;
import hcmut.cse.travelsocialnetwork.command.rate.CommandRate;
import hcmut.cse.travelsocialnetwork.model.Paginated;
import hcmut.cse.travelsocialnetwork.model.Rate;
import hcmut.cse.travelsocialnetwork.repository.rate.IRateRepository;
import hcmut.cse.travelsocialnetwork.service.redis.PostRedis;
import hcmut.cse.travelsocialnetwork.service.redis.RankRedis;
import hcmut.cse.travelsocialnetwork.service.redis.UserRedis;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import hcmut.cse.travelsocialnetwork.utils.enums.FactorialPost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    RankRedis rankRedis;
    INotificationApplication notificationApplication;

    public RateApplication(IRateRepository rateRepository,
                           PostRedis postRedis,
                           UserRedis userRedis,
                           RankRedis rankRedis,
                           INotificationApplication notificationApplication) {
        this.rateRepository = rateRepository;
        this.postRedis = postRedis;
        this.userRedis = userRedis;
        this.rankRedis = rankRedis;
        this.notificationApplication = notificationApplication;
    }

    @Override
    public Optional<Rate> mark(CommandRate commandRate) throws Exception {
        var query = new Document(Constant.FIELD_QUERY.USER_ID, commandRate.getUserId())
                .append(Constant.FIELD_QUERY.POST_ID, commandRate.getPostId());
        var rateOptional = rateRepository.get(query);
        if (rateOptional.isPresent()) {
            log.warn("rate is exist");
            throw new CustomException(Constant.ERROR_MSG.RATE_IS_EXIST);
        }

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
        var pointPostNew = postRedis.increaseAndGetPoints(commandRate.getPostId(), commandRate.getPoint());
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_POST, commandRate.getPostId(), pointPostNew);

        var pointUserNew = userRedis.increaseAndGetPoints(commandRate.getUserId(), Constant.POINTS.ONE_RATE_USER);
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_USER, commandRate.getUserId(), pointUserNew);

        var userTrigger = userRedis.getUser(commandRate.getUserId());
        var post = postRedis.getPost(commandRate.getPostId());
        var commandNotification = CommandNotification.builder()
                .userId(post.getUserId())
                .userIdTrigger(userTrigger.get_id().toString())
                .isRead(false)
                .objectId(post.get_id().toString())
                .type(Constant.NOTIFICATION.RATE)
                .channel(Constant.NOTIFICATION.RATE)
                .title(String.format(Constant.NOTIFICATION.TITLE_POST, post.getTitle()))
                .content(String.format(Constant.NOTIFICATION.CONTENT_RATE, userTrigger.getFullName(), post.getTitle()))
                .build();
        notificationApplication.createNotification(commandNotification);
        return rateAdd;
    }

    @Override
    public Optional<Rate> updateMark(CommandRate commandRate) throws Exception {
        var query = new Document(Constant.FIELD_QUERY.USER_ID, commandRate.getUserId())
                .append(Constant.FIELD_QUERY.POST_ID, commandRate.getPostId());
        var rateOptional = rateRepository.get(query);
        if (rateOptional.isEmpty()) {
            log.warn("rate not found");
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_RATE);
        }

        var pointPostNew = postRedis.updateAndGetPoints(commandRate.getPostId(), rateOptional.get().getPoint(), commandRate.getPoint());
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_POST, commandRate.getPostId(), pointPostNew);

        rateOptional.get().setPoint(commandRate.getPoint());
        return rateRepository.update(rateOptional.get().get_id().toString(), rateOptional.get());
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

        var pointPostNew = postRedis.decreaseAndGetPoints(commandRate.getPostId(),rate.get().getPoint());
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_POST, commandRate.getPostId(), pointPostNew);

        var pointUserNew = userRedis.decreaseAndGetPoints(commandRate.getUserId(), Constant.POINTS.ONE_RATE_USER);
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_USER, commandRate.getUserId(), pointUserNew);


        return rateRepository.delete(rate.get().get_id().toString());
    }



    @Override
    public Optional<Object> load(CommandRate commandRate) throws Exception {
        var query = new Document(Constant.FIELD_QUERY.POST_ID, commandRate.getPostId());
        var sort = new Document(Constant.FIELD_QUERY.CREATE_TIME, -1);
        var listRate = rateRepository.search(query, sort, commandRate.getPage(), commandRate.getSize());
        var totalItem = rateRepository.count(query);
        log.info(String.format("post %s have rate size %d", commandRate.getPostId(), totalItem.orElse(0L)));
        return Optional.of(new Paginated<>(listRate.orElse(new ArrayList<>()), commandRate.getPage(), commandRate.getSize(), totalItem.orElse(0L)));
    }
}
