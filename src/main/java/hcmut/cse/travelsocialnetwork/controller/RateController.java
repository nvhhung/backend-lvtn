package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.application.rate.IRateApplication;
import hcmut.cse.travelsocialnetwork.factory.AbstractController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 9/20/22 Tuesday
 **/
@Component
public class RateController extends AbstractController {
    private static final Logger log = LogManager.getLogger(RateController.class);
    IRateApplication rateApplication;

    public RateController(IRateApplication rateApplication) {
        this.rateApplication = rateApplication;
    }
}
