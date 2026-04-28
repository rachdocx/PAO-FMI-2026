package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.*;

import java.util.List;
public class SubscriptionService {
    private EntityManager em;

    public SubscriptionService(EntityManager em){
        this.em = em;
    }

    public void createSubscription(String name, double price){
        try{

            em.getTransaction().begin();

            Subscription new_sub = new Subscription(name, price);

            em.persist(new_sub);
            em.getTransaction().commit();
        }catch(Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public Subscription getSubscriptionById(int id){
        try{
            Subscription sub = em.find(Subscription.class, id);
            return sub;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Subscription> printAllSubscriptions(){
        TypedQuery<Subscription> query = em.createQuery("SELECT s FROM Subscription s", Subscription.class);
        List<Subscription> subs = query.getResultList();
        return subs;
    }

    public void deleteSubscription(int id){
        try{
            em.getTransaction().begin();
            Subscription sub = em.find(Subscription.class, id);
            if(sub != null){
                em.remove(sub);
                em.getTransaction().commit();
            }
        }catch(Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }
}
