package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import app.MainFX;
import service.UserService;

public class SignUpController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label mesajStatus;

    @FXML
    protected void onSignUpClick() {
        String user = usernameField.getText();
        String email = emailField.getText();
        String pass = passwordField.getText();

        UserService userService = new UserService(MainFX.em);

        try {
            userService.signUpUser(user, email, null, null, pass);
            mesajStatus.setStyle("-fx-text-fill: green;");
            mesajStatus.setText("Cont creat cu succes în baza de date!");
            usernameField.clear();
            emailField.clear();
            passwordField.clear();
        } catch (Exception e) {
            mesajStatus.setStyle("-fx-text-fill: red;");
            mesajStatus.setText("Eroare la creare cont!");
        }
    }
}