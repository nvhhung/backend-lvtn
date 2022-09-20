package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.application.like.ILikeApplication;
import hcmut.cse.travelsocialnetwork.factory.AbstractController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 9/20/22 Tuesday
 **/
@Component
public class LikeController extends AbstractController {
    private static final Logger log = LogManager.getLogger(LikeController.class);
    ILikeApplication likeApplication;

    public LikeController(ILikeApplication likeApplication) {
        this.likeApplication = likeApplication;
    }
}
