package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import models.*;

import java.security.spec.ECField;
import java.util.List;
public class SongService {

    private EntityManager em;

    public SongService(EntityManager em){
        this.em = em;
    }

    public List<Song> searchSongByName(String name){
        TypedQuery<Song> query = em.createQuery("SELECT s FROM Song s WHERE lower(s.file_name) LIKE lower(:name) ", Song.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    public void addSong(String file_name, int duration_seconds, String stream_url, int artist_id, String genre, int stream_count){
        try{
            em.getTransaction().begin();
            Artist temp_artist = em.find(Artist.class, artist_id);
            if(temp_artist != null){
                Song new_song = new Song(file_name, duration_seconds, stream_url, temp_artist, genre, stream_count);
                em.persist(new_song);
                em.getTransaction().commit();

            }
        }catch(Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public Song getSongById(int id){
        try{
            Song song = em.find(Song.class, id);
            return song;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
