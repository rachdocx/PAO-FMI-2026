package controllers;

import app.MainFX;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Advertisement;
import models.Song;
import models.User;
import service.AdvertisementService;
import service.AudioFileService;
import service.PlaylistService;
import service.SongService;
import javafx.scene.media.MediaPlayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Objects;
import exceptions.DatabaseOperationException;
import exceptions.ResourceNotFoundException;
import exceptions.SubscriptionLimitExceededException;

public class UserMainPageController {
    private User current_user;

    //done de facut logica pentru diferite subscriptii ce poate face un user DONE nu chiar ca da reclame doar in playlist if im not sure
    //done de facut logica si pentru delete doar pentru add
    //done de bagat alta metoda care afiseaza frumos cate secunde are piesa/album/artist/an_apartie_album
    //TODO de facut user profile unde iti pune poza/schimbat abonament
    //done DE ADAUGAT BUTOANE DE BACK SI LOGOUT CA E OBNOXIUS RAU SA TREBUIASCA SA OPRESC APLICATIA BFFR
    //done DE FACUT UN SEARCH ENGINE CARE SA NU FIE ENERVANT


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
    private Label currentAdLabel;
    @FXML
    private Label welcomeUser;

    @FXML
    private TextField search_field;
    @FXML
    private TextField playlist_nameField;
    @FXML
    private TextField editPlaylistName;

    @FXML
    private VBox addPlaylistPanel;
    @FXML
    private VBox PlaylistSongs;
    @FXML
    private VBox assignPlaylistPanel;
    @FXML
    private VBox editPlaylistPanel;


    @FXML
    private Button playButton;
    @FXML
    private Button skipButton;
    @FXML
    private Button prevButton;
    @FXML
    private Button editPlaylistButton;

    @FXML
    private ComboBox<String> playlistSelectionBox;


    private MediaPlayer mediaPlayer;
    private String currentStreamUrl;
    private boolean isPlaying = false;
    private AudioFileService currentService;
    private List<Advertisement> session_ads = new ArrayList<>();
    private int hour_limit = 20;
    private List<String> currentPlaylistUrls = new ArrayList<>();
    private List<String> getCurrentPlaylistSongNames = new ArrayList<>();
    private List<Integer> currnetPlaylistSongIds = new ArrayList<>();
    private int currentSongIndex;
    private List<Song> search_res;
    private int songs_played_session = 0;
    private int songs_played_hour = 0;
    private long hour = System.currentTimeMillis();
    private int ad_index = 0;
    private String selected_playlist;
    private int current_song_id;

    public void setUser(User user){
        this.current_user = user;
        welcomeUser.setText(user.getUsername());
        if((Objects.equals(this.current_user.getSubscription().getName(), "Free")) || (Objects.equals(this.current_user.getSubscription().getName(), "Premium")))
        {
            EntityManager em = MainFX.getEmf().createEntityManager();
            AdvertisementService advertisementService = new AdvertisementService(em);
            session_ads = advertisementService.loadAds();
        }
        loadUserPlaylists();

        //listener pentru selectare playlist
        playlistView.setOnMouseClicked(event ->{
            String selectedPlaylist = playlistView.getSelectionModel().getSelectedItem();
            if(selectedPlaylist != null){
                selected_playlist = selectedPlaylist;
                loadPlaylistTracks(selectedPlaylist);
                currentPlaylistLabel.setText(selectedPlaylist);

                editPlaylistButton.setManaged(true);
                editPlaylistButton.setVisible(true);
            }
        });

        //listener pentru redare piesa din playlist
        songPlaylistView.setOnMouseClicked(mouseEvent -> {
            currentPlaylistUrls.clear();
            getCurrentPlaylistSongNames.clear();

            String selectedSongFormatted = songPlaylistView.getSelectionModel().getSelectedItem();
            if(selectedSongFormatted != null){
                String actualSongName = selectedSongFormatted.split("   \\|   ")[0].trim();
                currentAudioLabel.setText(actualSongName);
                EntityManager em = MainFX.getEmf().createEntityManager();
                try{
                    SongService songService = new SongService(em);
                    List<Song> songs = songService.searchSongByName(actualSongName);

                    if(!songs.isEmpty()){
                        currentStreamUrl = songs.get(0).getStream_url();
                        current_song_id = songs.get(0).getId();
                        if(mediaPlayer != null){
                            mediaPlayer.stop();
                            mediaPlayer = null;
                            isPlaying = false;
                            playButton.setText("▶ Play");
                        }
                    }
                } catch (DatabaseOperationException | ResourceNotFoundException e) {
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
                        current_song_id = songs.get(0).getId();

                        if(mediaPlayer != null){
                            mediaPlayer.stop();
                            mediaPlayer = null;
                            isPlaying = false;
                            playButton.setText("▶ Play");
                        }
                    }
                } catch (DatabaseOperationException e) {
                    e.printStackTrace();
                } finally {
                    if (em != null && em.isOpen())
                        em.close();
                }
            }
        });
    }

    @FXML
    private void onLogOut(ActionEvent event) {
        try{
            this.current_user= null;

            if(mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer = null;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainPage.fxml"));
            Parent newRoot = loader.load();
            Scene current_scene = ((Node) event.getSource()).getScene();
            current_scene.setRoot(newRoot);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeletePlaylist(){
        EntityManager em = null;
        try{
            em = MainFX.getEmf().createEntityManager();
            PlaylistService playlistService = new PlaylistService(em);
            playlistService.deletePlaylist(selected_playlist, this.current_user.getId());

            selected_playlist = null;

            editPlaylistButton.setVisible(false);
            editPlaylistButton.setManaged(false);

            PlaylistSongs.setVisible(false);
            PlaylistSongs.setManaged(false);

            loadUserPlaylists();
        } catch (DatabaseOperationException e) {
            e.printStackTrace();
        } finally {
            if(em != null && em.isOpen())
                em.close();
        }
    }

    @FXML
    private void onPlayPlaylist(){
        String playlistToPlay = currentPlaylistLabel.getText();
        EntityManager em = null;

        try{

            em = MainFX.getEmf().createEntityManager();
            PlaylistService playlistService = new PlaylistService(em);
            List<Song> songs = playlistService.getPlaylistSongs(playlistToPlay, this.current_user.getId());

            if(songs == null || songs.isEmpty())
                return;

            currentPlaylistUrls.clear();
            getCurrentPlaylistSongNames.clear();

            for(Song s : songs){
                currentPlaylistUrls.add(s.getStream_url());
                getCurrentPlaylistSongNames.add(s.getFile_name());
                currnetPlaylistSongIds.add(s.getId());
            }

            currentSongIndex = 0;

            skipButton.setVisible(true);
            skipButton.setManaged(true);
            prevButton.setVisible(true);
            prevButton.setManaged(true);

            playNextSongInQueue();
        }catch (DatabaseOperationException e) {
            e.printStackTrace();
        } finally {
            if(em != null && em.isOpen())
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


        try {
            checkHourLimit();
        } catch (SubscriptionLimitExceededException e) {
            currentAudioLabel.setText(e.getMessage());
            if(mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer = null;
            }
            playButton.setText("▶ Play");
            isPlaying = false;
            return;
        }


        if(shouldPlayAd()){
            playAd(() -> {
                songs_played_hour++;
                songs_played_session++;
                actuallyPlaySong();
            });
            return;
        }


        songs_played_hour++;
        songs_played_session++;
        actuallyPlaySong();
    }

    private void actuallyPlaySong() {
        if (currentSongIndex >= currentPlaylistUrls.size() || currentSongIndex < 0) {
            playButton.setText("▶ Play");
            isPlaying = false;
            currentAudioLabel.setText("Playlist finished");
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }

        currentStreamUrl = currentPlaylistUrls.get(currentSongIndex);
        currentAudioLabel.setText(getCurrentPlaylistSongNames.get(currentSongIndex));

        try {
            EntityManager em = MainFX.getEmf().createEntityManager();
            currentService = new SongService(em);
            Song song = ((SongService)currentService).getSongById(currnetPlaylistSongIds.get(currentSongIndex));
            
            mediaPlayer = currentService.preparePlayer(song, () -> {
                currentSongIndex++;
                playNextSongInQueue();
            });

            currentAudioLabel.setText(currentService.getDisplayMessage(song));
            skipButton.setDisable(!currentService.isSkippable());
            prevButton.setDisable(!currentService.isSkippable());

            mediaPlayer.play();
            playButton.setText("⏸ Pause");
            isPlaying = true;

        } catch (Exception e) {
            currentSongIndex++;
            playNextSongInQueue();
        }
    }

    @FXML
    private void onSkipNext() {
        if (currentService != null) {
            currentService.handleSkipNext(mediaPlayer, () -> {
                if(!currentPlaylistUrls.isEmpty()) {
                    currentSongIndex++;
                    playNextSongInQueue();
                }
            });
        }
    }

    @FXML
    private void onSkipPrev() {
        if (currentService != null) {
            currentService.handleSkipPrev(mediaPlayer, () -> {
                if(!currentPlaylistUrls.isEmpty() && currentSongIndex > 0) {
                    currentSongIndex--;
                    playNextSongInQueue();
                }
            });
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
                checkHourLimit();
            } catch (SubscriptionLimitExceededException e) {
                currentAudioLabel.setText(e.getMessage());
                return;
            }

            if(shouldPlayAd()){
                String previousLabel = currentAudioLabel.getText();
                playAd(() -> {
                    currentAudioLabel.setText(previousLabel);
                    songs_played_hour++;
                    songs_played_session++;
                    playSingleSong();
                });
                return;
            }

            songs_played_hour++;
            songs_played_session++;
            playSingleSong();
        }
    }

    private void playSingleSong() {
        try {
            EntityManager em = MainFX.getEmf().createEntityManager();
            currentService = new SongService(em);
            Song song = ((SongService)currentService).getSongById(current_song_id);
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer = null;
            }
            mediaPlayer = currentService.preparePlayer(song, () -> {
                playButton.setText("▶ Play");
                isPlaying = false;
                mediaPlayer.stop();
                mediaPlayer = null;
            });
            
            currentAudioLabel.setText(currentService.getDisplayMessage(song));
            skipButton.setDisable(!currentService.isSkippable());
            prevButton.setDisable(!currentService.isSkippable());
            
            mediaPlayer.play();
            playButton.setText("⏸ Pause");
            isPlaying = true;
        } catch (DatabaseOperationException e) {
            e.printStackTrace();
        }
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
            loadPlaylistTracks(selected_playlist);
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
            Set<String> names = new TreeSet<>();

            for(var song : search_res){
                names.add(song.getFile_name());
            }
            search_results.getItems().setAll(names);


        }catch (DatabaseOperationException e){
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

            } catch (DatabaseOperationException | ResourceNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (em != null && em.isOpen())
                    em.close();
            }
        }
    }

    //logica pentru reclame
    private boolean shouldPlayAd(){
        String sub = this.current_user.getSubscription().getName();

        if(sub.equals("Premium Plus") || sub.equals("Premium PLUS")){
            return false;
        }
        if(sub.equals("Free")){
            if(songs_played_session > 0 && songs_played_session % 5 == 0){
                return true;
            }
            else
                return false;
        }
        if(sub.equals("Premium")){
            if(songs_played_session > 0 && songs_played_session % 10 == 0){
                return true;
            }
            else
                return false;
        }
        return false;
    }

    private void checkHourLimit() {
        String sub = this.current_user.getSubscription().getName();
        if(!sub.equals("Free"))
            return;

        long now = System.currentTimeMillis();
        if (now - hour >= 3_600_000) {
            hour = now;
            songs_played_hour = 0;
        }

        if (songs_played_hour >= 20) {
            throw new SubscriptionLimitExceededException("Reached hourly tracks limit for the Free Subscription");
        }
    }

    private void playAd(Runnable afterAd){
        if(session_ads.isEmpty()){
            afterAd.run();
            return;
        }

        Advertisement ad = session_ads.get(ad_index % session_ads.size());
        ad_index++;

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer = null;
        }

        try{
            EntityManager em = MainFX.getEmf().createEntityManager();
            currentService = new AdvertisementService(em);
            mediaPlayer = currentService.preparePlayer(ad, () -> {
                currentAdLabel.setManaged(false);
                currentAdLabel.setVisible(false);
                skipButton.setDisable(false);
                prevButton.setDisable(false);
                afterAd.run();
            });

            currentAudioLabel.setText(currentService.getDisplayMessage(ad));
            currentAdLabel.setText("Go to website!");
            currentAdLabel.setManaged(true);
            currentAdLabel.setVisible(true);

            currentAdLabel.setOnMouseClicked(event ->{
                String url = ad.getBrand_forwarding();
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                } catch (Exception e) {}
                currentAudioLabel.setCursor(javafx.scene.Cursor.HAND);
            });

            skipButton.setDisable(!currentService.isSkippable());
            prevButton.setDisable(!currentService.isSkippable());

            mediaPlayer.play();
            playButton.setText("Advertisement");
            isPlaying = true;
        }catch(DatabaseOperationException e){
            e.printStackTrace();
            afterAd.run();
        }
    }

    @FXML
    private void onEditPlaylist(){
        editPlaylistPanel.setManaged(true);
        editPlaylistPanel.setVisible(true);

    }
    @FXML
    private void onEditPlaylistClick(){
        String playlistName = editPlaylistName.getText();
        String currentPlaylist = selected_playlist;
        if(playlistName != null && currentPlaylist != null){
            try {
                EntityManager em = MainFX.getEmf().createEntityManager();
                PlaylistService playlistService = new PlaylistService(em);
                playlistService.editPlaylistName(currentPlaylist, this.current_user.getId(), playlistName);
                loadUserPlaylists();
                editPlaylistPanel.setVisible(false);
                editPlaylistPanel.setManaged(false);
            }catch(DatabaseOperationException e){
                e.printStackTrace();
            }
        }
    }
}
