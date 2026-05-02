package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import models.*;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;
public class SongService {

    private EntityManager em;

    public List<String> artistSongs(Artist artist){
        int artist_id = artist.getId();

        try{
            TypedQuery<Song> query = em.createQuery("SELECT s from Song s WHERE s.artist.id = :artist_id", Song.class);
            query.setParameter("artist_id", artist_id);

            List<Song> res = query.getResultList();
            List<String> sg_string = new ArrayList<String>();

            for(var sg : res){
                sg_string.add(sg.getFile_name());
            }
            return sg_string;
        }catch (NoResultException e){
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
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
