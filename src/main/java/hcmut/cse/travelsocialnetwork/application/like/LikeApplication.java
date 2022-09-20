package hcmut.cse.travelsocialnetwork.application.like;

import hcmut.cse.travelsocialnetwork.repository.like.ILikeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 9/20/22 Tuesday
 **/
@Component
public class LikeApplication implements ILikeApplication{
    private static final Logger log = LogManager.getLogger(LikeApplication.class);
    ILikeRepository likeRepository;

    public LikeApplication(ILikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }
}
