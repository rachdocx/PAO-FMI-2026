package models;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "audio_files")
public abstract class AudioFile implements Comparable<AudioFile> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(nullable = false)
    protected int duration_seconds;

    @Column(nullable = false)
    protected int stream_count;

    @Column(nullable = false)
    protected String file_name;

    @Column(nullable = false)
    protected String stream_url;

    protected AudioFile() {}

    public AudioFile(String file_name, int duration_seconds, String stream_url, int stream_count) {
        this.file_name = file_name;
        this.duration_seconds = duration_seconds;
        this.stream_url = stream_url;
        this.stream_count = stream_count;
    }

    public abstract void play();

    @Override
    public int compareTo(AudioFile other) {
        return Integer.compare(this.duration_seconds, other.duration_seconds);
    }

    public void incStreamCount(){
        this.stream_count++;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStream_url() {
        return stream_url;
    }

    public void setStream_url(String stream_url) {
        this.stream_url = stream_url;
    }

    public int getDuration_seconds() {
        return duration_seconds;
    }

    public void setDuration_seconds(int duration_seconds) {
        this.duration_seconds = duration_seconds;
    }

    public String getFile_name() {
        return file_name;
    }

    public int getStream_count() {
        return stream_count;
    }

    public void setStream_count(int stream_count) {
        this.stream_count = stream_count;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
