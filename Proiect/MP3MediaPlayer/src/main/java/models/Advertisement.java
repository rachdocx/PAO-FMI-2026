package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "advertisements")
public class Advertisement extends AudioFile{

    @Column(nullable = false)
    String brand_name;

    @Column(nullable = true)
    String brand_forwarding;

    @Override
    public void play(){
        System.out.println("reclama");
    }

    public Advertisement(){}

    public Advertisement(String file_name, int duration_seconds, String stream_url, int stream_count, String brand_name, String brand_forwarding){
        super(file_name, duration_seconds, stream_url, stream_count);
        this.brand_forwarding = brand_forwarding;
        this.brand_name = brand_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_forwarding() {
        return brand_forwarding;
    }

    public void setBrand_forwarding(String brand_forwarding) {
        this.brand_forwarding = brand_forwarding;
    }
}
