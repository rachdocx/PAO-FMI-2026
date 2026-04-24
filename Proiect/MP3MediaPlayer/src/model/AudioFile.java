package model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AudioFile implements Comparable<AudioFile> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    protected int duration_seconds;
    protected int stream_count;
    protected String file_name;
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

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
