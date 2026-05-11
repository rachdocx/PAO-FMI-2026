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

    public AlbumService(EntityManager em) {
        this.em = em;
    }

    public List<String> albumTracks(String albumTitle, int artistId) {
        try {
            TypedQuery<Album> query = em.createQuery("SELECT a FROM Album a WHERE a.title = :title AND a.artist.id = :artistId", Album.class);
            query.setParameter("title", albumTitle);
            query.setParameter("artistId", artistId);
            Album album = query.getSingleResult();

            List<String> tracks = new ArrayList<>();
            for (Song s : album.getTracklist()) {
                tracks.add(s.getFile_name());
            }
            return tracks;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void deleteAlbum(String album_title, int id_artist){
        try{
            em.getTransaction().begin();
            TypedQuery<Album> query = em.createQuery("SELECT a FROM Album a WHERE a.title = :album_title AND a.artist.id = :id_artist", Album.class);
            query.setParameter("album_title", album_title);
            query.setParameter("id_artist", id_artist);

            Album album = query.getSingleResult();
            em.remove(album);
            em.getTransaction().commit();

        }catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void addAlbum(String title, int release_year, Artist artist) {
        try {
            em.getTransaction().begin();
            Artist managedArtist = em.find(Artist.class, artist.getId());
            Album new_album = new Album(title, managedArtist, release_year, null);
            em.persist(new_album);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public List<String> artistAlbums(Artist artist) {
        int artist_id = artist.getId();

        try {
            TypedQuery<Album> query = em.createQuery("SELECT a from Album a WHERE a.artist.id = :artist_id", Album.class);
            query.setParameter("artist_id", artist_id);

            List<Album> res = query.getResultList();
            List<String> al_string = new ArrayList<String>();

            for (var al : res) {
                al_string.add(al.getTitle());
            }
            return al_string;
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // TODO: DE FACUT FINALLY CU INCHIS LA BAZA DE DATE
    }

    public void assignSongToAlbum(String songTitle, String albumTitle, int artistId) {
        try {
            em.getTransaction().begin();
            TypedQuery<Song> songQuery = em.createQuery("SELECT s FROM Song s WHERE s.file_name = :songTitle AND s.artist.id = :artistId", Song.class);
            songQuery.setParameter("songTitle", songTitle);
            songQuery.setParameter("artistId", artistId);
            Song song = songQuery.getSingleResult();

            TypedQuery<Album> albumQuery = em.createQuery("SELECT a FROM Album a WHERE a.title = :albumTitle AND a.artist.id = :artistId", Album.class);
            albumQuery.setParameter("albumTitle", albumTitle);
            albumQuery.setParameter("artistId", artistId);
            Album album = albumQuery.getSingleResult();

            song.setAlbum(album);
            em.merge(song);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

}
