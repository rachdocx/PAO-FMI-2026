package utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneChanger {
    public static void changeScene(ActionEvent event, String fxml_path){
        try{
            Parent newRoot = FXMLLoader.load(SceneChanger.class.getResource(fxml_path));

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(newRoot);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
