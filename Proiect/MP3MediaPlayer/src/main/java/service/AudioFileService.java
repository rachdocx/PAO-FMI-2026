package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import models.*;
public class AudioFileService {
    private EntityManager em;

    public AudioFileService(EntityManager em){
        this.em = em;
    }

    public void addAudioFile(String file_name, int duration_seconds, String stream_url, int stream_count){

    }

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
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
