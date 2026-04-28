package controllers;

import app.MainFX;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.User;
import service.UserService;
import utils.SceneChanger;

public class SignInController {
    @FXML private TextField email_field;
    @FXML private TextField password_field;
    @FXML private Label auth_state;

    @FXML
    protected void onSignInClick(ActionEvent event){
        String email = email_field.getText();
        String pass = password_field.getText();

        EntityManager em = null;
        try{
            em = MainFX.getEmf().createEntityManager();
            UserService userService = new UserService(em);
            User auth = userService.signInUser(email, pass);
            if(auth != null){
                auth_state.setStyle("-fx-text-fill: green");
                auth_state.setText("Welcome back " + auth.getUsername());
                email_field.clear();
                password_field.clear();

                SceneChanger.changeScene(event, "/views/UserMainPage.fxml");


            }
            else{
                auth_state.setStyle("-fx-text-fill: red");
                auth_state.setText("Password or Email are wrong!");
            }
        }catch(Exception e){
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
}
