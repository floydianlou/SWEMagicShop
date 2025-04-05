package GUI.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChooseRegistrationTypeController {

    @FXML private ImageView shopIcon;

    public void initialize() {
        shopIcon.setImage(new Image(getClass().getResource("/images/shop-icon.png").toExternalForm())); }

    @FXML
    private void handleClientRegistration() {
        SceneController.loadScene("user-registration.fxml");
    }

    @FXML
    private void handleManagerRegistration() {
        SceneController.loadScene("manager-registration.fxml");
    }

    @FXML
    private void handleBackToMenu() {
        SceneController.loadScene("main-view.fxml");
    }

}


