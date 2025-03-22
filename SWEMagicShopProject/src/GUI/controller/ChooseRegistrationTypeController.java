package GUI.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;

public class ChooseRegistrationTypeController {

    @FXML private ImageView shopIcon;

    public void initialize() {
        shopIcon.setImage(new Image(getClass().getResource("/images/shop-icon.png").toExternalForm())); }

    @FXML
    private void handleClientRegistration() {
        SceneController.loadScene("user-registration-view.fxml");
    }

    @FXML
    private void handleManagerRegistration() {
        SceneController.loadScene("manager-registration.fxml");
    }

    @FXML
    private void handleBack() {
        SceneController.loadScene("main-view.fxml");
    }

}


