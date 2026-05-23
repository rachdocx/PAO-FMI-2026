package service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import models.Artist;
import models.Song;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import exceptions.DatabaseOperationException;
import exceptions.ResourceNotFoundException;
import models.AudioFile;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

public class SongService extends AudioFileService {

    public SongService(EntityManager em) {
        super(em);
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
        return true;
    }

    @Override
    public String getDisplayMessage(AudioFile file) {
        return file.getFile_name();
    }

    @Override
    public void handleSkipNext(MediaPlayer player, Runnable playNextInQueue) {
        if (player != null)
            player.stop();
        if (playNextInQueue != null)
            playNextInQueue.run();
    }

    @Override
    public void handleSkipPrev(MediaPlayer player, Runnable playPrevInQueue) {
        if (player != null)
            player.stop();
        if (playPrevInQueue != null)
            playPrevInQueue.run();
    }

    public void deleteSong(String song_title, int id_artist) {
        try {
            em.getTransaction().begin();
            TypedQuery<Song> query = em.createQuery("SELECT s FROM Song s WHERE s.file_name = :file_name and s.artist.id = :id_artist", Song.class);
            query.setParameter("file_name", song_title);
            query.setParameter("id_artist", id_artist);
            Song song = query.getSingleResult();
            em.createNativeQuery("DELETE FROM playlist_songs WHERE song_id = ?1").setParameter(1, song.getId()).executeUpdate();
            em.remove(song);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw new DatabaseOperationException("Error deleting song", e);
        }
    }

    public void removeSongFromAlbum(String song_title, int id_artist) {
        try {
            em.getTransaction().begin();
            TypedQuery<Song> query = em.createQuery(
                    "SELECT s FROM Song s WHERE s.artist.id = :id_artist and s.file_name = :file_name", Song.class);
            // ciudat mod de a retine datele, probleme daca exista doua piese cu aceleasi
            // nume ale aceluias artist dar putin probabil
            query.setParameter("id_artist", id_artist);
            query.setParameter("file_name", song_title);
            Song song = query.getSingleResult();
            song.setAlbum(null);
            em.merge(song);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw new DatabaseOperationException("Error removing song from album", e);
        }
    }

    public Set<String> artistSongs(Artist artist) {
        int artist_id = artist.getId();

        try {
            TypedQuery<Song> query = em.createQuery("SELECT s from Song s WHERE s.artist.id = :artist_id", Song.class);
            query.setParameter("artist_id", artist_id);

            List<Song> res = query.getResultList();
            Set<String> sg_string = new TreeSet<String>();

            for (var sg : res) {
                sg_string.add(sg.getFile_name() + "   |    Plays: " + sg.getStream_count());
            }
            return sg_string;
        } catch (NoResultException e) {
            throw new ResourceNotFoundException("No songs found for artist");
        } catch (Exception e) {
            throw new DatabaseOperationException("Error fetching artist songs", e);
        }
    }

    public List<Song> searchSongByName(String name) {
        String formattedSearch = name + "%";
        TypedQuery<Song> query = em.createQuery(
                "SELECT s FROM Song s WHERE lower(s.file_name) LIKE lower(:name) ", Song.class);
        query.setParameter("name", formattedSearch);
        return query.getResultList();
    }

    public void addSong(String file_name, int duration_seconds, String stream_url, int artist_id, String genre,
            int stream_count) {
        try {
            em.getTransaction().begin();
            Artist temp_artist = em.find(Artist.class, artist_id);
            if (temp_artist != null) {
                Song new_song = new Song(file_name, duration_seconds, stream_url, temp_artist, genre, stream_count);
                em.persist(new_song);
                em.getTransaction().commit();

            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new DatabaseOperationException("Error adding song", e);
        }
    }

    public Song getSongById(int id) {
        try {
            Song song = em.find(Song.class, id);
            if (song == null) {
                throw new ResourceNotFoundException("Song not found with id: " + id);
            }
            return song;
        } catch (Exception e) {
            throw new DatabaseOperationException("Error fetching song by id", e);
        }
    }

}
