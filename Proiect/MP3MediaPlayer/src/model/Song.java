package model;

import jakarta.persistence.*;

@Entity
@Table(name = "songs")
public class Song extends AudioFile{
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;
    private String genre;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    public Song() {}

    public Song(String file_name, int duration_seconds, String stream_url, Artist artist, String genre, int stream_count) {
        super(file_name, duration_seconds, stream_url, stream_count);
        this.artist = artist;
        this.genre = genre;
    }

    @Override
    public void play() {
        System.out.println("Playing song: " + file_name + " by " + artist.getUsername());
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
