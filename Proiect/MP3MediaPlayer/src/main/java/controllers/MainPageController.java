package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import utils.SceneChanger;

import javax.swing.*;

public class MainPageController {
    @FXML
    private Button sign_in_button;

    @FXML
    private Button log_in_button;

    public void handleSignIn(ActionEvent event){
        SceneChanger.changeScene(event, "/views/SignInView.fxml");
    }

    public void handleSignUp(ActionEvent event){
        SceneChanger.changeScene(event, "/views/SignUpView.fxml");
    }



}
