package GUI.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChooseRegistrationTypeController {

    @FXML private ImageView shopIcon;
    @FXML private ImageView shopIcon2;

    public void initialize() {
        Image icon = new Image(getClass().getResource("/images/shop-icon.png").toExternalForm());
        shopIcon.setImage(icon);
        shopIcon2.setImage(icon);
    }

    @FXML
    private void handleClientRegistration() {
        SceneController.loadScene("user-registration-view.fxml");
    }

    @FXML
    private void handleManagerRegistration() {
        SceneController.loadScene("manager-authentication-view.fxml");
    }

    @FXML
    private void handleBack() {
        SceneController.loadScene("welcome-view.fxml");
    }

}