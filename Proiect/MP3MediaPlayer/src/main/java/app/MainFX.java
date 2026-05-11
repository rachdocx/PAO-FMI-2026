package app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    public static EntityManagerFactory emf;
    public static EntityManager em;

    public static EntityManagerFactory getEmf() {
        return emf;
    }

    @Override
    public void start(Stage stage) throws Exception {

        emf = Persistence.createEntityManagerFactory("spotify-pu");
        em = emf.createEntityManager();

        // Seed advertisements at startup
        EntityManager seedEm = emf.createEntityManager();
        try {
            new service.AdvertisementService(seedEm).seedAds();
        } finally {
            seedEm.close();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MainPage.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Rachify");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        if (em != null)
            em.close();
        if (emf != null)
            emf.close();
    }

    public static void main(String[] args) {
        launch();
    }
}