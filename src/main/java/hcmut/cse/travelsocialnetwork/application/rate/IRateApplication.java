package hcmut.cse.travelsocialnetwork.application.rate;

import hcmut.cse.travelsocialnetwork.command.rate.CommandRate;
import hcmut.cse.travelsocialnetwork.model.Rate;

import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 9/20/22 Tuesday
 **/
public interface IRateApplication {
    Optional<Rate> mark(CommandRate commandRate) throws Exception;
    Optional<Rate> updateMark(CommandRate commandRate) throws Exception;
    Optional<Boolean> unmark(CommandRate commandRate) throws Exception;
    Optional<Object> load(CommandRate commandRate) throws Exception;
}
