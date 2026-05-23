package service;

import jakarta.persistence.EntityManager;
import javafx.scene.media.MediaPlayer;
import models.*;
import exceptions.DatabaseOperationException;

public abstract class AudioFileService {
    protected EntityManager em;

    public AudioFileService(EntityManager em){
        this.em = em;
    }

    public void addAudioFile(String file_name, int duration_seconds, String stream_url, int stream_count){
    }

    public abstract MediaPlayer preparePlayer(AudioFile file, Runnable onFinish);
    public abstract boolean isSkippable();
    public abstract String getDisplayMessage(AudioFile file);
    public abstract void handleSkipNext(MediaPlayer player, Runnable playNextInQueue);
    public abstract void handleSkipPrev(MediaPlayer player, Runnable playPrevInQueue);

    public void incStreamCount(int id){
        try{
            em.getTransaction().begin();
            AudioFile audioFile = em.find(AudioFile.class, id);
            audioFile.incStreamCount();
            em.getTransaction().commit();
        }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new DatabaseOperationException("Error incrementing stream count", e);
        } finally {
            em.close();
        }
    }
}
