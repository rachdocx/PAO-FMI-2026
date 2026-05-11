package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import models.Playlist;
import models.Song;
import models.User;
import java.util.ArrayList;
import java.util.List;

public class PlaylistService {

    private final EntityManager em;

    public PlaylistService(EntityManager em){
        this.em = em;
    }

    // metoda ce returneaza melodiile unui playlist ca string-uri
    public List<String> playlistTracks(String playlistTitle, int userId) {
        try {
            TypedQuery<Playlist> query = em.createQuery("SELECT p FROM Playlist p WHERE p.playlist_name = :title AND p.owner.id = :userid", Playlist.class);

            query.setParameter("title", playlistTitle);
            query.setParameter("userid", userId);

            Playlist playlist = query.getSingleResult();
            List<String> trackNames = new ArrayList<>();

            for (var song : playlist.getTracklist()) {
                trackNames.add(song.getFile_name());
            }

            return trackNames;
        } catch (NoResultException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // metoda ce returneaza melodiile unui playlist ca obiecte
    public List<Song> getPlaylistSongs(String playlistTitle, int userId){
        try{
            TypedQuery<Playlist> query = em.createQuery("SELECT p FROM Playlist p WHERE p.playlist_name = :title AND p.owner.id = :userid", Playlist.class);

            query.setParameter("title", playlistTitle);
            query.setParameter("userid", userId);

            Playlist playlist = query.getSingleResult();

            return playlist.getTracklist();
        } catch (NoResultException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //metoda ce adauga un playlist
    public void addPlaylist(String name, int user_id){
        try{
            em.getTransaction().begin();
            User user = em.find(User.class, user_id);
            Playlist playlist = new Playlist(name, user, null);
            em.persist(playlist);
            em.getTransaction().commit();
        }catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    //de sters
//    public void addSongToPlaylist(int playlist_id, int song_id){
//        try {
//            em.getTransaction().begin();
//
//            Playlist playlist = em.find(Playlist.class, playlist_id);
//            Song song = em.find(Song.class, song_id);
//
//            if (playlist != null && song != null) {
//                playlist.getTracklist().add(song);
//                em.merge(playlist);
//            }
//        }
//        catch(Exception e){
//            if(em.getTransaction().isActive())
//                em.getTransaction().rollback();
//            e.printStackTrace();
//        }
//    }

    //metoda ce return
    public List<String> userPlaylists(User user) {
        int user_id = user.getId();

        try {
            TypedQuery<Playlist> query = em.createQuery("SELECT p FROM Playlist p WHERE p.owner.id = :id_user", Playlist.class);

            query.setParameter("id_user", user_id);

            List<Playlist> res = query.getResultList();
            List<String> ps_string = new ArrayList<String>();
            for(var ps : res){
                ps_string.add(ps.getPlaylist_name());
            }

            return ps_string;
        } catch (NoResultException e){
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void addSongToPlaylistByName(String songName, String playlistName, int userId) {
        try {
            em.getTransaction().begin();
            TypedQuery<Song> songQ = em.createQuery("SELECT s FROM Song s WHERE s.file_name = :songName", Song.class);
            songQ.setParameter("songName", songName);
            Song song = songQ.getSingleResult();
            TypedQuery<Playlist> playQ = em.createQuery("SELECT p FROM Playlist p WHERE p.playlist_name = :pName AND p.owner.id = :uid", Playlist.class);
            playQ.setParameter("pName", playlistName);
            playQ.setParameter("uid", userId);
            Playlist playlist = playQ.getSingleResult();
            playlist.getTracklist().add(song);
            em.merge(playlist);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

}
