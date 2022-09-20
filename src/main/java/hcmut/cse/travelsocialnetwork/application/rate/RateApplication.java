package hcmut.cse.travelsocialnetwork.application.rate;

import hcmut.cse.travelsocialnetwork.repository.rate.IRateRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 9/20/22 Tuesday
 **/
@Component
public class RateApplication implements IRateApplication{

    private static final Logger log = LogManager.getLogger(RateApplication.class);
    IRateRepository rateRepository;

    public RateApplication(IRateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }
}
