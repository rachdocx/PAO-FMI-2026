package controllers;

import app.MainFX;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Artist;
import models.User;
import service.UserService;
import exceptions.AuthenticationException;
import exceptions.DatabaseOperationException;

public class SignInController {
    @FXML private TextField email_field;
    @FXML private TextField password_field;
    @FXML private Label auth_state;

    @FXML
    protected void onSignInClick(ActionEvent event){
        String email = email_field.getText();
        String pass = password_field.getText();

        EntityManager em = null;
        try {
            em = MainFX.getEmf().createEntityManager();
            UserService userService = new UserService(em);
            try {
                Artist artist = userService.signInArtist(email, pass);
                auth_state.setStyle("-fx-text-fill: green");
                auth_state.setText("Welcome back artist " + artist.getUsername());
                email_field.clear();
                password_field.clear();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ArtistMainPage.fxml"));
                Parent newRoot = loader.load();
                ArtistMainPageController ctrl = loader.getController();
                ctrl.setArtist(artist);
                Scene current_scene = ((Node) event.getSource()).getScene();
                current_scene.setRoot((newRoot));
            } catch (AuthenticationException e1) {
                try {
                    User auth = userService.signInUser(email, pass);
                    auth_state.setStyle("-fx-text-fill: green");
                    auth_state.setText("Welcome back " + auth.getUsername());
                    email_field.clear();
                    password_field.clear();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserMainPage.fxml"));
                    Parent newRoot = loader.load();
                    UserMainPageController ctrl = loader.getController();
                    ctrl.setUser(auth);
                    Scene current_scene = ((Node) event.getSource()).getScene();
                    current_scene.setRoot((newRoot));
                } catch (AuthenticationException e2) {
                    auth_state.setStyle("-fx-text-fill: red");
                    auth_state.setText("Password or Email are wrong!");
                }
            }
        } catch (DatabaseOperationException e) {
            e.printStackTrace();
            auth_state.setStyle("-fx-text-fill: red");
            auth_state.setText("A database error occurred during sign-in.");
        } catch (Exception e) {
            e.printStackTrace();
            auth_state.setStyle("-fx-text-fill: red");
            auth_state.setText("An error occurred during sign-in.");
        }
        finally {
            if(em != null && em.isOpen()){
                em.close();
            }
        }
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainPage.fxml"));
            Parent newRoot = loader.load();
            Scene current_scene = ((Node) event.getSource()).getScene();
            current_scene.setRoot(newRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
