package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Subscription;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.NoResultException;
import exceptions.DatabaseOperationException;
import exceptions.ResourceNotFoundException;
public class SubscriptionService {
    private EntityManager em;

    public SubscriptionService(EntityManager em){
        this.em = em;
    }

    public void createSubscription(String name, double price, String description){
        try{

            em.getTransaction().begin();
            Subscription new_sub = new Subscription(name, price, description);
            em.persist(new_sub);
            em.getTransaction().commit();
        }catch(Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new DatabaseOperationException("Error creating subscription", e);
        }
    }

    public Subscription getSubscriptionById(int id){
        try{
            Subscription sub = em.find(Subscription.class, id);
            if (sub == null) throw new ResourceNotFoundException("Subscription not found");
            return sub;
        }catch(Exception e){
            throw new DatabaseOperationException("Error fetching subscription", e);
        }
    }

    public List<String> printAllSubscriptions(){
        TypedQuery<Subscription> query = em.createQuery("SELECT s FROM Subscription s", Subscription.class);
        List<Subscription> subs = query.getResultList();
        List<String> subs_name = new ArrayList<>();

        for(var sub : subs){
            String textElement = sub.getName() + " - " + sub.getPrice() + "$ (" + sub.getDescription() + ")";
            subs_name.add(textElement);
        }
        return subs_name;
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
            throw new DatabaseOperationException("Error deleting subscription", e);
        }
    }

    public Subscription getSubscriptionByName(String name){
        try {
            TypedQuery<Subscription> query = em.createQuery("SELECT s FROM Subscription s WHERE s.name = :name", Subscription.class);
            query.setParameter("name", name);

            Subscription subscription = query.getSingleResult();

            if (subscription != null)
                return subscription;
        } catch (NoResultException e) {
            throw new ResourceNotFoundException("Subscription not found by name");
        } catch (Exception e) {
            throw new DatabaseOperationException("Error fetching subscription by name", e);
        }
        return null;
    }
}
