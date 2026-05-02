package models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "albums")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Column(nullable = false)
    private int release_year;

    @OneToMany(mappedBy = "album")
    private List<Song> tracklist;

    public Album() {}

    public Album(String title, Artist artist, int release_year, List<Song> tracklist) {
        this.title = title;
        this.artist = artist;
        this.release_year = release_year;
        this.tracklist = tracklist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public List<Song> getTracklist() {
        return tracklist;
    }

    public void setTracklist(List<Song> tracklist) {
        this.tracklist = tracklist;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
