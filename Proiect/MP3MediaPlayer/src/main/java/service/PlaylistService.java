package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import models.Playlist;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class PlaylistService {
    private EntityManager em;

    public PlaylistService(EntityManager em){
        this.em = em;
    }
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
}
