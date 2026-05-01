package controllers;

import app.MainFX;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import models.Playlist;
import models.User;
import service.PlaylistService;
import java.util.List;

public class UserMainPageController {
    private User current_user;

    @FXML
    private ListView<String> playlistView;

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
