package hcmut.cse.travelsocialnetwork.application.media;

import hcmut.cse.travelsocialnetwork.command.media.CommandMedia;
import hcmut.cse.travelsocialnetwork.model.Media;
import hcmut.cse.travelsocialnetwork.repository.media.IMediaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 10/19/22 Wednesday
 **/
@Component
public class MediaApplication implements IMediaApplication {
    private static final Logger log = LogManager.getLogger(MediaApplication.class);
    IMediaRepository mediaRepository;

    public MediaApplication(IMediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }


    @Override
    public Optional<Media> add(CommandMedia commandMedia) {
        var media = Media.builder()
            .postId(commandMedia.getPostId())
            .link(commandMedia.getLink())
            .type(commandMedia.getType())
            .build();
        return mediaRepository.add(media);
    }

    @Override
    public Optional<List<Media>> loadByPostId(CommandMedia commandMedia) {
        return mediaRepository.search(new Document("postId", commandMedia.getPostId()), new Document(), commandMedia.getPage(), commandMedia.getSize());
    }

    @Override
    public Optional<Boolean> delete(String mediaId) {
        return mediaRepository.delete(mediaId);
    }
}
