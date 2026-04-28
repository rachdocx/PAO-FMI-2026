package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import app.MainFX;
import service.UserService;

import jakarta.persistence.EntityManager;
import utils.SceneChanger;

import javafx.event.ActionEvent;

public class SignUpController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label mesajStatus;

    @FXML
    protected void onSignUpClick(ActionEvent event) {
        String user = usernameField.getText();
        String email = emailField.getText();
        String pass = passwordField.getText();

        EntityManager em = null;
        try {
            em = MainFX.getEmf().createEntityManager();
            UserService userService = new UserService(em);
            userService.signUpUser(user, email, null, null, pass);
            mesajStatus.setStyle("-fx-text-fill: green;");
            mesajStatus.setText("Account successfully created");
            usernameField.clear();
            emailField.clear();
            passwordField.clear();
            SceneChanger.changeScene(event, "/views/SignInView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            mesajStatus.setStyle("-fx-text-fill: red;");
            mesajStatus.setText("An unexpected error occurred.");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}