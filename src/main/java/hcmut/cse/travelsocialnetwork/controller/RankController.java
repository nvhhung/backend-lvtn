package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.application.rank.IRankApplication;
import hcmut.cse.travelsocialnetwork.command.post.CommandPost;
import hcmut.cse.travelsocialnetwork.command.rank.CommandRank;
import hcmut.cse.travelsocialnetwork.factory.AbstractController;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 9/22/22 Thursday
 **/
@Component
public class RankController extends AbstractController {
    private static final Logger log = LogManager.getLogger(RankController.class);
    IRankApplication rankApplication;

    public RankController(IRankApplication rankApplication) {
        this.rankApplication = rankApplication;
    }

    public void getLeaderBoardUser(RoutingContext routingContext) {
        try {
            MultiMap params = routingContext.request().params();
            var page = Integer.parseInt(Optional.ofNullable(params.get("page")).orElse("1"));
            var size = Integer.parseInt(Optional.ofNullable(params.get("size")).orElse("20"));
            var commandRank = CommandRank.builder()
                    .page(page)
                    .size(size)
                    .build();
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, rankApplication.getLeaderBoardUser(commandRank)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void getRankUser(RoutingContext routingContext) {
        try {
            var commandRank = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandRank.class);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, rankApplication.getRankUser(commandRank)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void getLeaderBoardPost(RoutingContext routingContext) {
        try {
            MultiMap params = routingContext.request().params();
            var page = Integer.parseInt(Optional.ofNullable(params.get("page")).orElse("1"));
            var size = Integer.parseInt(Optional.ofNullable(params.get("size")).orElse("20"));
            var commandRank = CommandRank.builder()
                    .page(page)
                    .size(size)
                    .build();
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, rankApplication.getLeaderBoardPost(commandRank)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void getRankPost(RoutingContext routingContext) {
        try {
            var commandRank = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandRank.class);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, rankApplication.getRankPost(commandRank)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }
}
