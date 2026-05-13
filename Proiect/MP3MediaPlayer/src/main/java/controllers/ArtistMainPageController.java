package controllers;

import app.MainFX;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Artist;
import service.AlbumService;

import javafx.scene.control.ListView;
import service.SongService;

import java.awt.event.ActionEvent;
import java.util.List;

public class ArtistMainPageController {
    private Artist current_artist;
    private String current_album;

    // TODO de facut logica si pentru delete doar pentru add

    @FXML
    private ListView<String> albumView;
    @FXML
    private ListView<String> songView;
    @FXML
    private ListView<String> songAlbumView;

    @FXML
    private VBox assignAlbumPanel;
    @FXML
    private VBox AlbumSongs;
    @FXML
    private VBox addSongPanel;
    @FXML
    private VBox addAlbumPanel;

    @FXML
    private TextField titleField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField durationField;
    @FXML
    private TextField urlField;
    @FXML
    private TextField albumTitleField;
    @FXML
    private TextField release_yearField;

    @FXML
    private Label selectedSongLabel;
    @FXML
    private Label albumTracksLabel;

    @FXML
    private ComboBox<String> albumSelectionBox;

    @FXML
    private Button deleteAlbumButton;
    @FXML
    private Button deleteSongButton;
    @FXML
    private Button removeFromAlbumButton;

    @FXML
    private VBox songDetails;

    @FXML
    private void onLogOut(javafx.event.ActionEvent event) {
        try{
            this.current_artist = null;
            this.current_album = null;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainPage.fxml"));
            Parent newRoot = loader.load();
            Scene current_scene = ((Node) event.getSource()).getScene();

            current_scene.setRoot(newRoot);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeleteAlbum() {
        String selectedAlbum = albumView.getSelectionModel().getSelectedItem();
        EntityManager em = null;
        try {
            em = MainFX.getEmf().createEntityManager();
            AlbumService albumService = new AlbumService(em);
            albumService.deleteAlbum(selectedAlbum, current_artist.getId());

            AlbumSongs.setVisible(false);
            AlbumSongs.setManaged(false);

            deleteAlbumButton.setVisible(false);
            deleteAlbumButton.setManaged(false);

            loadArtistAlbums();
            loadArtistSongs();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    @FXML
    private void onDeleteSong(){
        String selectedSong = songView.getSelectionModel().getSelectedItem();
        EntityManager em = null;
        try {
            em = MainFX.getEmf().createEntityManager();
            SongService songService = new SongService(em);
            songService.deleteSong(selectedSong, current_artist.getId());

            deleteSongButton.setVisible(false);
            deleteSongButton.setManaged(false);

            loadArtistSongs();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(em != null && em.isOpen())
                em.close();
        }
    }
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
                if (em != null && em.isOpen())
                    em.close();
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

            deleteAlbumButton.setVisible(false);
            deleteAlbumButton.setManaged(false);

            loadArtistAlbums();
        } catch (Exception e) {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    @FXML
    private void onRemoveFromAlbum(){
        String selectedSong = songAlbumView.getSelectionModel().getSelectedItem();
        EntityManager em = null;
        try{
            em = MainFX.getEmf().createEntityManager();
            SongService songService = new SongService(em);
            songService.removeSongFromAlbum(selectedSong, this.current_artist.getId());

            removeFromAlbumButton.setVisible(false);
            removeFromAlbumButton.setManaged(false);

            loadAlbumTracks(current_album);
        }catch (Exception e) {
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
            if (selectedTitle != null) {
                loadAlbumTracks(selectedTitle);
                current_album = selectedTitle;
                deleteAlbumButton.setVisible(true);
                deleteAlbumButton.setManaged(true);

                songDetails.setVisible(false);
                songDetails.setManaged(false);


            }
        });

        songAlbumView.setOnMouseClicked(event -> {
            String selectedSong = songAlbumView.getSelectionModel().getSelectedItem();
            if(selectedSong != null){
                removeFromAlbumButton.setVisible(true);
                removeFromAlbumButton.setManaged(true);

                songDetails.setVisible(false);
                songDetails.setManaged(false);
            }
        });

        songView.setOnMouseClicked(event -> {
            String selectedSong = songView.getSelectionModel().getSelectedItem();
            if (selectedSong != null) {
                selectedSongLabel.setText(selectedSong);

                songDetails.setVisible(true);
                songDetails.setManaged(true);

                assignAlbumPanel.setVisible(true);
                assignAlbumPanel.setManaged(true);

                deleteSongButton.setVisible(true);
                deleteSongButton.setManaged(true);

                removeFromAlbumButton.setVisible(false);
                removeFromAlbumButton.setManaged(false);
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
