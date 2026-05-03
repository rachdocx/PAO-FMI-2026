package controllers;

import app.MainFX;
import com.sun.tools.javac.Main;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private VBox assignAlbumPanel;

    @FXML
    private ComboBox<String> albumSelectionBox;

    @FXML
    private Label selectedSongLabel;

    @FXML
    private VBox AlbumSongs;

    @FXML
    private ListView<String> songAlbumView;

    @FXML
    private Label albumTracksLabel;

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
    private void onAssignToAlbumClick() {
        String selectedSong = selectedSongLabel.getText();
        String selectedAlbum = albumSelectionBox.getValue();

        if (selectedSong != null && selectedAlbum != null) {
            EntityManager em = null;
            try {
                em = MainFX.getEmf().createEntityManager();
                AlbumService albumService = new AlbumService(em);
                albumService.assignSongToAlbum(selectedSong, selectedAlbum, current_artist.getId());

                assignAlbumPanel.setVisible(false);
                assignAlbumPanel.setManaged(false);

                String currentAlbumView = albumView.getSelectionModel().getSelectedItem();
                if (selectedAlbum.equals(currentAlbumView)) {
                    loadAlbumTracks(selectedAlbum);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (em != null && em.isOpen()) em.close();
            }
        }
    }

    @FXML
    private void onAddSongPanelClick() {
        boolean show = !addSongPanel.isVisible();
        addSongPanel.setVisible(show);
        addSongPanel.setManaged(show);
    }

    @FXML
    private void onAddAlbumPanelClick() {
        boolean show = !addAlbumPanel.isVisible();
        addAlbumPanel.setVisible(show);
        addAlbumPanel.setManaged(show);
    }

    @FXML
    private void onAddAlbumClick() {
        String album_title = albumTitleField.getText();
        int release_year = Integer.parseInt(release_yearField.getText());

        EntityManager em = null;

        try {
            em = MainFX.getEmf().createEntityManager();
            AlbumService album_service = new AlbumService(em);

            album_service.addAlbum(album_title, release_year, this.current_artist);
            albumTitleField.clear();
            release_yearField.clear();

            addAlbumPanel.setVisible(false);
            addAlbumPanel.setManaged(false);

            loadArtistAlbums();
        } catch (Exception e) {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    @FXML
    private void onAddSongClick() {
        String title = titleField.getText();
        String genre = genreField.getText();
        int duration = Integer.parseInt(durationField.getText());
        String url = urlField.getText();

        EntityManager em = null;
        try {
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
        } catch (Exception e) {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void setArtist(Artist artist) {
        this.current_artist = artist;
        loadArtistSongs();
        loadArtistAlbums();

        albumView.setOnMouseClicked(event -> {
            String selectedTitle = albumView.getSelectionModel().getSelectedItem();
            if (selectedTitle != null)
                loadAlbumTracks(selectedTitle);
        });

        songView.setOnMouseClicked(event -> {
            String selectedSong = songView.getSelectionModel().getSelectedItem();
            if(selectedSong != null) {
                selectedSongLabel.setText(selectedSong);
                assignAlbumPanel.setVisible(true);
                assignAlbumPanel.setManaged(true);
            }
        });
    }

    private void loadAlbumTracks(String selectedTitle) {
        EntityManager em = null;

        try {
            em = MainFX.getEmf().createEntityManager();
            AlbumService album_service = new AlbumService(em);
            List<String> tracks = album_service.albumTracks(selectedTitle, current_artist.getId());
            AlbumSongs.setVisible(true);
            AlbumSongs.setManaged(true);
            songAlbumView.getItems().setAll(tracks);
        } catch (Exception e) {
            if (em != null && em.isOpen())
                em.close();
        }

    }

    public List<String> loadArtistSongs() {
        EntityManager em = null;

        try {
            em = MainFX.getEmf().createEntityManager();
            SongService song_service = new SongService(em);

            List<String> songs = song_service.artistSongs(this.current_artist);

            if (songs != null) {
                songView.getItems().setAll(songs);
            }
        } catch (Exception e) {
            if (em != null && em.isOpen())
                em.close();
        }
        return null;
    }

    public List<String> loadArtistAlbums() {
        EntityManager em = null;

        try {
            em = MainFX.getEmf().createEntityManager();
            AlbumService album_service = new AlbumService(em);

            List<String> albums = album_service.artistAlbums(this.current_artist);

            if (albums != null) {
                albumView.getItems().setAll(albums);
                albumSelectionBox.getItems().setAll(albums);
            }

        } catch (Exception e) {
            if (em != null && em.isOpen())
                em.close();
        }
        return null;
    }

}
