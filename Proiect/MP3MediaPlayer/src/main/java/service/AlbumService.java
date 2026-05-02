package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import models.Album;
import models.Artist;
import models.Song;

import java.util.ArrayList;
import java.util.List;

public class AlbumService {
    private EntityManager em;

    public AlbumService(EntityManager em){
        this.em = em;
    }

    public void addAlbum(String title, int release_year, Artist artist){
        try {
            em.getTransaction().begin();
            Artist managedArtist = em.find(Artist.class, artist.getId());
            Album new_album = new Album(title, managedArtist, release_year, null);
            em.persist(new_album);
            em.getTransaction().commit();
        } catch (Exception e){
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public List<String> artistAlbums(Artist artist){
        int artist_id = artist.getId();

        try{
            TypedQuery<Album> query = em.createQuery("SELECT a from Album a WHERE a.artist.id = :artist_id", Album.class);
            query.setParameter("artist_id", artist_id);

            List<Album> res = query.getResultList();
            List<String> al_string = new ArrayList<String>();

            for(var al : res){
                al_string.add(al.getTitle());
            }
            return al_string;
        }catch (NoResultException e){
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

        //TODO: DE FACUT FINALLY CU INCHIS LA BAZA DE DATE
    }


}
