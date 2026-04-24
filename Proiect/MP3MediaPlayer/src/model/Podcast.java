package model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "podcasts")
public class Podcast extends AudioFile{

    @ManyToOne
    @JoinColumn(name = "host_id")
    private Artist host;
    private String topic;

    @ElementCollection
    @CollectionTable(name = "podcast_guests", joinColumns = @JoinColumn(name = "podcast_id"))
    @Column(name = "guest")
    private List<String> guests = new ArrayList<>();

    public Podcast() {}

    public Podcast(String file_name, int duration_seconds, String stream_url, Artist host, String topic, List<String> guests, int stream_count) {
        super(file_name, duration_seconds, stream_url, stream_count);
        this.host = host;
        this.topic = topic;
        this.guests = guests;
    }

    @Override
    public void play() {
        System.out.println("Playing podcast: " + file_name + " hosted by " + host.getUsername() + " on topic: " + topic);
    }

    public Artist getHost() {
        return host;
    }

    public void setHost(Artist host) {
        this.host = host;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<String> getGuests() {
        return guests;
    }

    public void setGuests(List<String> guests) {
        this.guests = guests;
    }
}
