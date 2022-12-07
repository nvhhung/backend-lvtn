package hcmut.cse.travelsocialnetwork.application.like;

import hcmut.cse.travelsocialnetwork.command.like.CommandLike;
import hcmut.cse.travelsocialnetwork.model.Like;

import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 9/20/22 Tuesday
 **/
public interface ILikeApplication {
    Optional<Like> createLike(CommandLike commandLike) throws Exception;
    Optional<Boolean> unlike(CommandLike commandLike) throws Exception;
    Optional<Object> loadLike(CommandLike commandLike) throws Exception;
}
