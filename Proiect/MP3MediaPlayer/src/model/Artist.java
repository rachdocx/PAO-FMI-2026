package model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "artists")
@PrimaryKeyJoinColumn(name = "user_id")
public class Artist extends User{
    private int monthly_listeners;
    private String scene_name;

    @OneToMany(mappedBy = "artist")
    private List<Album> albums;

    public Artist() {}

    public Artist(int id, String username, String email, Subscription subscription, int monthly_listeners, String scene_name, List<Album> albums, String password, List<Playlist> created_playlists) {
        super(id, username, email, subscription, created_playlists, password);
        this.monthly_listeners = monthly_listeners;
        this.scene_name = scene_name;
        this.albums = albums;
    }

    public int getMonthly_listeners() {
        return monthly_listeners;
    }

    public void setMonthly_listeners(int monthly_listeners) {
        this.monthly_listeners = monthly_listeners;
    }

    public String getScene_name() {
        return scene_name;
    }

    public void setScene_name(String scene_name) {
        this.scene_name = scene_name;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
}
