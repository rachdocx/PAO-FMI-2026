package controllers;

import app.MainFX;
import com.sun.tools.javac.Main;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import models.Artist;
import service.AlbumService;
import service.PlaylistService;

import javafx.scene.control.ListView;
import service.SongService;

import java.util.List;

public class ArtistMainPageController {
    private Artist current_artist;

    @FXML
    private TextField titleField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField durationField;
    @FXML
    private TextField urlField;
    @FXML
    private VBox addSongPanel;
    @FXML
    private VBox addAlbumPanel;
    @FXML
    private ListView<String> albumView;

    @FXML
    private ListView<String> songView;

    @FXML
    private TextField albumTitleField;

    @FXML
    private TextField release_yearField;

    @FXML
    private void onAddSongPanelClick(){
        boolean show = !addSongPanel.isVisible();
        addSongPanel.setVisible(show);
        addSongPanel.setManaged(show);
    }

    @FXML
    private void onAddAlbumPanelClick(){
        boolean show = !addAlbumPanel.isVisible();
        addAlbumPanel.setVisible(show);
        addAlbumPanel.setManaged(show);
    }

    @FXML
    private void onAddAlbumClick(){
        String album_title = albumTitleField.getText();
        int release_year = Integer.parseInt(release_yearField.getText());

        EntityManager em = null;

        try{
            em = MainFX.getEmf().createEntityManager();
            AlbumService album_service = new AlbumService(em);

            album_service.addAlbum(album_title, release_year, this.current_artist);
            albumTitleField.clear();
            release_yearField.clear();

            addAlbumPanel.setVisible(false);
            addAlbumPanel.setManaged(false);

            loadArtistAlbums();
        } catch (Exception e) {
            if(em != null && em.isOpen())
                em.close();
        }
    }
    @FXML
    private void onAddSongClick(){
        String title = titleField.getText();
        String genre = genreField.getText();
        int duration = Integer.parseInt(durationField.getText());
        String url = urlField.getText();

        EntityManager em = null;
        try{
            em = MainFX.getEmf().createEntityManager();
            SongService song_service = new SongService(em);
            song_service.addSong(title, duration, url, this.current_artist.getId(), genre, 0);

            titleField.clear();
            genreField.clear();
            durationField.clear();
            urlField.clear();

            addSongPanel.setVisible(false);
            addSongPanel.setManaged(false);

            loadArtistSongs();
        }catch (Exception e){
            if(em != null && em.isOpen())
                em.close();
        }
    }

    public void setArtist(Artist artist){
        this.current_artist = artist;
        loadArtistSongs();
        loadArtistAlbums();
    }
    public List<String> loadArtistSongs(){
        EntityManager em = null;

        try{
            em = MainFX.getEmf().createEntityManager();
            SongService song_service = new SongService(em);

            List<String> songs = song_service.artistSongs(this.current_artist);

            if(songs != null){
                songView.getItems().setAll(songs);
            }
        }catch (Exception e){
            if(em != null && em.isOpen())
                em.close();
        }
        return null;
    }

    public List<String> loadArtistAlbums(){
        EntityManager em = null;

        try{
            em = MainFX.getEmf().createEntityManager();
            AlbumService album_service = new AlbumService(em);

            List<String> albums = album_service.artistAlbums(this.current_artist);

            if(albums != null){
                albumView.getItems().setAll(albums);
            }

        }
        catch (Exception e){
            if(em != null && em.isOpen())
                em.close();
        }
        return null;
    }

}
