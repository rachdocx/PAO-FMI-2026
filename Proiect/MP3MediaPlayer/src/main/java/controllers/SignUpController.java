package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import app.MainFX;
import models.Subscription;
import service.SubscriptionService;
import service.UserService;

import jakarta.persistence.EntityManager;
import utils.SceneChanger;

import javafx.event.ActionEvent;

import java.util.List;

public class SignUpController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label mesajStatus;

    @FXML private CheckBox artistCheckBox;
    @FXML private TextField sceneNameField;

    @FXML private CheckBox userCheckBox;
    @FXML private Label userSubscriptionLabel;
    @FXML private ListView<String> subscriptionList;

    @FXML
    private void onUserCheckBoxClick(){
        boolean is_user = userCheckBox.isSelected();

        sceneNameField.setVisible(false);
        sceneNameField.setManaged(false);

        artistCheckBox.setSelected(false);

        userSubscriptionLabel.setVisible(is_user);
        userSubscriptionLabel.setManaged(is_user);

        subscriptionList.setVisible(is_user);
        subscriptionList.setManaged(is_user);

        if(is_user)
            loadSubscriptions();
    }
    @FXML
    private void onArtistCheckBoxClick(){
        boolean is_artist = artistCheckBox.isSelected();

        userSubscriptionLabel.setVisible(false);
        userSubscriptionLabel.setManaged(false);

        subscriptionList.setVisible(false);
        subscriptionList.setManaged(false);

        userCheckBox.setSelected(false);

        sceneNameField.setVisible(is_artist);
        sceneNameField.setManaged(is_artist);
    }
    @FXML
    protected void onSignUpClick(ActionEvent event) {
        String user = usernameField.getText();
        String email = emailField.getText();
        String pass = passwordField.getText();


        EntityManager em = null;
        try {
            em = MainFX.getEmf().createEntityManager();
            UserService userService = new UserService(em);
            SubscriptionService subscriptionService = new SubscriptionService(em);
            if(artistCheckBox.isSelected()){
                userService.signUpArtist(user, email, pass, sceneNameField.getText());
            }
            else {
                String sub = subscriptionList.getSelectionModel().getSelectedItem();
                if (sub == null) {
                    mesajStatus.setStyle("-fx-text-fill: red;");
                    mesajStatus.setText("Te rugam sa alegi o subscriptie din lista!");
                    return;
                }
                String sub_name = sub.split(" - ")[0];
                Subscription userChoice = subscriptionService.getSubscriptionByName(sub_name);
                userService.signUpUser(user, email, userChoice, null, pass);
            }
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

    private void loadSubscriptions(){
        EntityManager em = null;
        try{
            em = MainFX.getEmf().createEntityManager();
            SubscriptionService subscriptionService = new SubscriptionService(em);
            List<String> subs = subscriptionService.printAllSubscriptions();

            if(subs != null){
                subscriptionList.getItems().setAll(subs);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(em != null && em.isOpen())
                em.close();
        }
    }
}