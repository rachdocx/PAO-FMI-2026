package models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @OneToMany(mappedBy = "owner")
    private List<Playlist> created_playlists;

    public User() {
        created_playlists = new java.util.ArrayList<>();
    }

    public User(int id, String username, String email, Subscription subscription, List<Playlist> created_playlists, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.subscription = subscription;
        this.created_playlists = created_playlists;
        this.password = password;
    }

    public User( String username, String email, Subscription subscription, List<Playlist> created_playlists, String password) {
        this.username = username;
        this.email = email;
        this.subscription = subscription;
        this.created_playlists = created_playlists;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public List<Playlist> getCreated_playlists() {
        return created_playlists;
    }

    public void setCreated_playlists(List<Playlist> created_playlists) {
        this.created_playlists = created_playlists;
    }
}
