package service;

import jakarta.persistence.EntityManager;
import controllers.UserMainPageController;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import models.AudioFile;

public class PodcastService extends AudioFileService {

    public PodcastService(EntityManager em) {
        super(em);
    }

    @Override
    public MediaPlayer preparePlayer(AudioFile file, Runnable onFinish) {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(file.getStream_url()));
        if (onFinish != null) {
            mediaPlayer.setOnEndOfMedia(onFinish);
        }
        return mediaPlayer;
    }

    @Override
    public boolean isSkippable() {
        return true;
    }

    @Override
    public String getDisplayMessage(AudioFile file) {
        return "Podcast: " + file.getFile_name();
    }

    @Override
    public void handleSkipNext(MediaPlayer player, Runnable playNextInQueue) {
        if (player != null) {
            player.seek(player.getCurrentTime().add(javafx.util.Duration.seconds(15)));
        }
    }

    @Override
    public void handleSkipPrev(MediaPlayer player, Runnable playPrevInQueue) {
        if (player != null) {
            player.seek(player.getCurrentTime().subtract(javafx.util.Duration.seconds(15)));
        }
    }

}
