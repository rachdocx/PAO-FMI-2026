package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import models.Artist;
import models.Playlist;
import models.Subscription;
import models.User;

import java.util.List;
import java.util.Objects;

public class UserService {


    private EntityManager em;

    public UserService(EntityManager em) {
        this.em = em;
    }
    public Artist signInArtist(String email, String password) {
        try {
            TypedQuery<Artist> query = em.createQuery(
                    "SELECT a FROM Artist a WHERE a.email = :email AND a.password = :password", Artist.class
            );
            query.setParameter("email", email);
            query.setParameter("password", password);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void signUpArtist(String username, String email, String password, String scene_name){
        try{
            em.getTransaction().begin();
            Artist artist = new Artist();
            artist.setUsername(username);
            artist.setEmail(email);
            artist.setScene_name(scene_name);
            artist.setMonthly_listeners(0);
            artist.setPassword(password);
            em.persist(artist);
            em.getTransaction().commit();
        }catch (Exception e){
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void signUpUser(String username, String email, Subscription subscription, List<Playlist> created_playlists, String password){

        try{
            em.getTransaction().begin();
            User new_user = new User(username, email, subscription, created_playlists, password);


            em.persist(new_user);
            em.getTransaction().commit();

        }catch(Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public void signUpUserFree(String username, String email, Subscription subscription, List<Playlist> created_playlists, String password){
        try{

            em.getTransaction().begin();
            Subscription free_sub = new Subscription("Free", 0.00);
            User new_user = new User(username, email, subscription, created_playlists, password);
            new_user.setSubscription(free_sub);
            em.persist(new_user);
            em.getTransaction().commit();

        }catch(Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public User signInUser(String email, String password) {
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            return query.getSingleResult();


        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUserById(int id){
        try{
            User user = em.find(User.class, id);
            return user;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<User> printAllUsers(){
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        List<User> users = query.getResultList();
        return users;
    }

    public void deleteUser(int userId) {
        EntityTransaction tx = em.getTransaction();
        try {
                tx.begin();
                User user = em.find(User.class, userId);
                if (user != null) {
                    em.remove(user);
            }
            tx.commit();
            System.out.println("Utilizatorul cu ID " + userId + " a fost șters.");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }



}
