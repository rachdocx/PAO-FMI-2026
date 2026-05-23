package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Advertisement;
import models.AudioFile;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import java.util.List;
import exceptions.DatabaseOperationException;

public class AdvertisementService extends AudioFileService {
    public AdvertisementService(EntityManager em) {
        super(em);
    }

    public List<Advertisement> loadAds() {
        try {
            TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a", Advertisement.class);

            List<Advertisement> all_ads = query.getResultList();

            return all_ads;
        } catch (Exception e) {
            throw new DatabaseOperationException("Error loading ads", e);
        }
    }

    @Override
    public MediaPlayer preparePlayer(AudioFile file, Runnable onFinish) {
        incStreamCount(file.getId());
        
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(file.getStream_url()));
        if (onFinish != null) {
            mediaPlayer.setOnEndOfMedia(onFinish);
        }

        return mediaPlayer;
    }

    @Override
    public boolean isSkippable() {
        return false;
    }

    @Override
    public String getDisplayMessage(AudioFile file) {
        return "Advertisement: " + ((Advertisement)file).getBrand_name();
    }

    @Override
    public void handleSkipNext(MediaPlayer player, Runnable playNextInQueue) {
        //de adaugat eroare ca nu poti da skip la ad
    }

    @Override
    public void handleSkipPrev(MediaPlayer player, Runnable playPrevInQueue) {
        //de adaugat eroare ca nu poti da skip la ad
    }

    public void seedAds() {
        try {
            long count = em.createQuery("SELECT COUNT(a) FROM Advertisement a", Long.class).getSingleResult();
            if (count > 0) {
                return;
            }

            em.getTransaction().begin();

            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("src/main/resources/ads_seed.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 5) {
                        Advertisement ad = new Advertisement(
                            parts[0], 
                            Integer.parseInt(parts[1]), 
                            parts[2], 
                            0, 
                            parts[3], 
                            parts[4]
                        );
                        em.persist(ad);
                    }
                }
            } catch (java.io.IOException e) {
                System.err.println("Failed to read ads_seed.txt: " + e.getMessage());
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw new DatabaseOperationException("Error seeding ads", e);
        }
    }
}
