package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.factory.AbstractController;
import hcmut.cse.travelsocialnetwork.repository.post.IPostRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 8/30/22 Tuesday
 **/
@Component
public class PostController extends AbstractController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    @Autowired
    IPostRepository postRepository;

}
