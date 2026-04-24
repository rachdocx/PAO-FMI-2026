package model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "playlist_name")
    private String playlist_name;

    @ManyToMany
    @JoinTable(
        name = "playlist_songs",
        joinColumns = @JoinColumn(name = "playlist_id"),
        inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> tracklist;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    public Playlist() {}

    public Playlist(int id, String playlist_name, List<Song> tracklist) {
        this.id = id;
        this.playlist_name = playlist_name;
        this.tracklist = tracklist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Song> getTracklist() {
        return tracklist;
    }

    public void setTracklist(List<Song> tracklist) {
        this.tracklist = tracklist;
    }

    public String getPlaylist_name() {
        return playlist_name;
    }

    public void setPlaylist_name(String playlist_name) {
        this.playlist_name = playlist_name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
