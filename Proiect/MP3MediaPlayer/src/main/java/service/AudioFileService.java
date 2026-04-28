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
}
