package controllers;

import app.MainFX;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Song;
import models.User;
import service.PlaylistService;
import service.SongService;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.ArrayList;
import java.util.List;

public class UserMainPageController {
    private User current_user;

    @FXML
    private ListView<String> playlistView;
    @FXML
    private ListView<String> search_results;
    @FXML
    private ListView<String> songPlaylistView;


    @FXML
    private Label currentPlaylistLabel;
    @FXML
    private Label currentAudioLabel;
    @FXML
    private Label selectedSongLabel;


    @FXML
    private TextField search_field;
    @FXML
    private TextField playlist_nameField;

    @FXML
    private VBox addPlaylistPanel;
    @FXML
    private VBox PlaylistSongs;
    @FXML
    private VBox assignPlaylistPanel;


    @FXML
    private Button playButton;
    @FXML
    private Button skipButton;
    @FXML
    private Button prevButton;

    @FXML
    private ComboBox<String> playlistSelectionBox;


    private MediaPlayer mediaPlayer;
    private String currentStreamUrl;
    private boolean isPlaying = false;



    private List<String> currentPlaylistUrls = new ArrayList<>();
    private List<String> getCurrentPlaylistSongNames = new ArrayList<>();
    private int currentSongIndex;

    private List<Song> search_res;


    @FXML
    private void onPlayPlaylist(){
        String playlistToPlay = currentPlaylistLabel.getText();
        EntityManager em = MainFX.getEmf().createEntityManager();
        try{
            PlaylistService playlistService = new PlaylistService(em);
            List<Song> songs = playlistService.getPlaylistSongs(playlistToPlay, this.current_user.getId());

            if(songs == null || songs.isEmpty())
                return;

            currentPlaylistUrls.clear();
            getCurrentPlaylistSongNames.clear();

            for(Song s : songs){
                currentPlaylistUrls.add(s.getStream_url());
                getCurrentPlaylistSongNames.add(s.getFile_name());
            }

            currentSongIndex = 0;

            skipButton.setVisible(true);
            skipButton.setManaged(true);
            prevButton.setVisible(true);
            prevButton.setManaged(true);

            playNextSongInQueue();
        }finally{
            em.close();
        }
    }

    private void playNextSongInQueue() {
        if (currentSongIndex >= currentPlaylistUrls.size() || currentSongIndex < 0) {
            if(mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer = null;
            }
            playButton.setText("▶ Play");
            isPlaying = false;
            currentAudioLabel.setText("Playlist finished");

            skipButton.setVisible(false);
            skipButton.setManaged(false);
            prevButton.setVisible(false);
            prevButton.setManaged(false);
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }

        currentStreamUrl = currentPlaylistUrls.get(currentSongIndex);
        currentAudioLabel.setText(getCurrentPlaylistSongNames.get(currentSongIndex));

        try {
            Media media = new Media(currentStreamUrl);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();

            playButton.setText("⏸ Pause");
            isPlaying = true;

            mediaPlayer.setOnEndOfMedia(() -> {
                currentSongIndex++;
                playNextSongInQueue();
            });

        } catch (Exception e) {
            currentSongIndex++;
            playNextSongInQueue();
        }
    }

    @FXML
    private void onSkipNext() {
        if(!currentPlaylistUrls.isEmpty()) {
            currentSongIndex++;
            playNextSongInQueue();
        }
    }

    @FXML
    private void onSkipPrev() {
        if(!currentPlaylistUrls.isEmpty() && currentSongIndex > 0) {
            currentSongIndex--;
            playNextSongInQueue();
        }
    }

    //pentru redare doar o melodie
    @FXML
    private void onPlayClick() {

        skipButton.setVisible(false);
        skipButton.setManaged(false);
        prevButton.setVisible(false);
        prevButton.setManaged(false);

        if (currentStreamUrl == null || currentStreamUrl.isEmpty()) {
            return;
        }

        if (mediaPlayer != null) {
            if (isPlaying) {
                mediaPlayer.pause();
                playButton.setText("▶ Play");
                isPlaying = false;
            } else {
                mediaPlayer.play();
                playButton.setText("⏸ Pause");
                isPlaying = true;
            }
        } else {
            try {
                Media media = new Media(currentStreamUrl);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();

                playButton.setText("⏸ Pause");
                isPlaying = true;

                mediaPlayer.setOnEndOfMedia(() -> {
                    playButton.setText("▶ Play");
                    isPlaying = false;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setUser(User user){
        this.current_user = user;
        loadUserPlaylists();

        //listener pentru selectare playlist
        playlistView.setOnMouseClicked(event ->{
            String selectedPlaylist = playlistView.getSelectionModel().getSelectedItem();
            if(selectedPlaylist != null){
                loadPlaylistTracks(selectedPlaylist);
                currentPlaylistLabel.setText(selectedPlaylist);
            }
        });

        //listener pentru redare piesa din playlist
        songPlaylistView.setOnMouseClicked(mouseEvent -> {
            currentPlaylistUrls.clear();
            getCurrentPlaylistSongNames.clear();

            String selectedSong = songPlaylistView.getSelectionModel().getSelectedItem();
            if(selectedSong != null){
                currentAudioLabel.setText(selectedSong);
                EntityManager em = MainFX.getEmf().createEntityManager();
                try{
                    SongService songService = new SongService(em);
                    List<Song> songs = songService.searchSongByName(selectedSong);

                    if(!songs.isEmpty()){
                        currentStreamUrl = songs.get(0).getStream_url();
                        if(mediaPlayer != null){
                            mediaPlayer.stop();
                            mediaPlayer = null;
                            isPlaying = false;
                            playButton.setText("▶ Play");
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (em != null && em.isOpen())
                        em.close();
                }
            }
        });

        //listener pentru muzica din search bar
        search_results.setOnMouseClicked(event -> {
            currentPlaylistUrls.clear();
            getCurrentPlaylistSongNames.clear();

            String selectedSong = search_results.getSelectionModel().getSelectedItem();
            if(selectedSong != null) {
                selectedSongLabel.setText(selectedSong);
                assignPlaylistPanel.setVisible(true);
                assignPlaylistPanel.setManaged(true);
                currentPlaylistLabel.setText("From searches");

                currentAudioLabel.setText(selectedSong);
                EntityManager em = MainFX.getEmf().createEntityManager();
                try{
                    SongService songService = new SongService(em);
                    List<Song> songs = songService.searchSongByName(selectedSong);

                    if(!songs.isEmpty()){
                        currentStreamUrl = songs.get(0).getStream_url();
                        if(mediaPlayer != null){
                            mediaPlayer.stop();
                            mediaPlayer = null;
                            isPlaying = false;
                            playButton.setText("▶ Play");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (em != null && em.isOpen())
                        em.close();
                }
            }
        });
    }

    //logica de dat load la track uri albume
    private void loadPlaylistTracks(String seelectedPlaylist){
        EntityManager em = null;
        try{
            em = MainFX.getEmf().createEntityManager();
            PlaylistService playlist_service = new PlaylistService(em);
            List<String> tracks = playlist_service.playlistTracks(seelectedPlaylist, this.current_user.getId());
            PlaylistSongs.setVisible(true);
            PlaylistSongs.setManaged(true);
            songPlaylistView.getItems().setAll(tracks);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    //logica de dat load la playlist uri
    public void loadUserPlaylists(){
        EntityManager em = null;
        try{
            em = MainFX.getEmf().createEntityManager();
            PlaylistService playlist_service = new PlaylistService(em);

            List<String> playlists = playlist_service.userPlaylists(this.current_user);

            if(playlists != null){
                playlistView.getItems().setAll(playlists);
                playlistSelectionBox.getItems().setAll(playlists);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(em != null && em.isOpen())
                em.close();
        }
    }

    //logica pentru adaugat un playlist
    @FXML
    private void onAddPlaylistPanelClick(){
        boolean show = !addPlaylistPanel.isVisible();
        addPlaylistPanel.setVisible(show);
        addPlaylistPanel.setManaged(show);
    }

    @FXML
    public void onAddPlaylistClick(){
        String playlist_name = playlist_nameField.getText();

        EntityManager em = null;
        try{
            em = MainFX.getEmf().createEntityManager();
            PlaylistService playlist_service = new PlaylistService(em);
            playlist_service.addPlaylist(playlist_name, this.current_user.getId());

            playlist_nameField.clear();
            addPlaylistPanel.setManaged(false);
            addPlaylistPanel.setVisible(false);
            loadUserPlaylists();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    //logica de search la o melodie
    @FXML
    private void onSearchClick(){
        String query = search_field.getText();
        EntityManager em = null;
        try{
            em = MainFX.getEmf().createEntityManager();
            SongService service = new SongService(em);
            search_res = service.searchSongByName(query);
            List<String> names = new ArrayList<>();

            for(var song : search_res){
                names.add(song.getFile_name());
            }
            search_results.getItems().setAll(names);


        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(em != null && em.isOpen())
                em.close();
        }

    }

    //logica de dat play la o melodie
    @FXML
    private void onAssignToPlaylistClick() {
        String selectedSong = selectedSongLabel.getText();
        String selectedPlaylist = playlistSelectionBox.getValue();

        if (selectedSong != null && selectedPlaylist != null) {
            EntityManager em = null;
            try {
                em = MainFX.getEmf().createEntityManager();
                PlaylistService playlistService = new PlaylistService(em);
                playlistService.addSongToPlaylistByName(selectedSong, selectedPlaylist, current_user.getId());
                assignPlaylistPanel.setVisible(false);
                assignPlaylistPanel.setManaged(false);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (em != null && em.isOpen())
                    em.close();
            }
        }
    }
}
