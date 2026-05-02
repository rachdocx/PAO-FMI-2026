package controllers;

import app.MainFX;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Playlist;
import models.Song;
import models.User;
import service.PlaylistService;
import service.SongService;

import java.util.ArrayList;
import java.util.List;

public class UserMainPageController {
    private User current_user;

    @FXML
    private ListView<String> playlistView;
    @FXML
    private TextField search_field;
    @FXML
    private ListView<String> search_results;

    private List<Song> search_res;

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
                search_results.getItems().add(song.getFile_name());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(em != null && em.isOpen())
                em.close();
        }

    }

    public void setUser(User user){
        this.current_user = user;
        loadUserPlaylists();
    }

    public List<String> loadUserPlaylists(){
        EntityManager em = null;
        try{
            em = MainFX.getEmf().createEntityManager();
            PlaylistService playlist_service = new PlaylistService(em);

            List<String> playlists = playlist_service.userPlaylists(this.current_user);

            if(playlists != null){
                playlistView.getItems().setAll(playlists);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(em != null && em.isOpen())
                em.close();
        }
        return null;
    }
}
