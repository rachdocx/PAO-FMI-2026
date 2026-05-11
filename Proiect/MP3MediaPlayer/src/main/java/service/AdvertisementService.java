package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Advertisement;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementService {
    private EntityManager em;

    public AdvertisementService(EntityManager em){
        this.em = em;
    }

    public List<Advertisement> loadAds(){
        try{
            TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a", Advertisement.class);

            List<Advertisement> all_ads = query.getResultList();

            return all_ads;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void seedAds(){
        try{
            long count = em.createQuery("SELECT COUNT(a) FROM Advertisement a", Long.class).getSingleResult();
            if(count > 0){
                System.out.println("Advertisements already seeded (" + count + " found).");
                return;
            }

            em.getTransaction().begin();

            Advertisement ad1 = new Advertisement("Spotify Premium Ad", 15, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3", 0, "Spotify", "https://spotify.com");
            Advertisement ad2 = new Advertisement("Nike Just Do It", 10, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3", 0, "Nike", "https://nike.com");
            Advertisement ad3 = new Advertisement("Coca Cola Ad", 20, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3", 0, "Coca-Cola", "https://coca-cola.com");
            Advertisement ad4 = new Advertisement("Samsung Galaxy Ad", 12, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3", 0, "Samsung", "https://samsung.com");
            Advertisement ad5 = new Advertisement("McDonalds Lovin It", 8, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3", 0, "McDonalds", "https://mcdonalds.com");

            em.persist(ad1);
            em.persist(ad2);
            em.persist(ad3);
            em.persist(ad4);
            em.persist(ad5);

            em.getTransaction().commit();
            System.out.println("Seeded 5 advertisements.");
        }catch(Exception e){
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
