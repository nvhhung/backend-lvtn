package hcmut.cse.travelsocialnetwork.application.media;

import hcmut.cse.travelsocialnetwork.command.media.CommandMedia;
import hcmut.cse.travelsocialnetwork.model.Media;

import java.util.List;
import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 10/19/22 Wednesday
 **/
public interface IMediaApplication {
    Optional<Media> add(CommandMedia commandMedia);
    Optional<List<Media>> load(CommandMedia commandMedia);
    Optional<Boolean> delete(String mediaId);
}
