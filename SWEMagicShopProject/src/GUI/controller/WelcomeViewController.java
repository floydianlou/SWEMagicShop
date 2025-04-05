package GUI.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WelcomeViewController {

    @FXML private ImageView iconImageView;

    @FXML
    public void initialize() {
        Image icon = new Image(getClass().getResource("/images/shop-icon.png").toExternalForm());
        iconImageView.setImage(icon);
    }

    @FXML
    private void handleLoginButton() {
        SceneController.loadScene("login-view.fxml");
    }


    @FXML
    public void handleRegisterChoice() {
        SceneController.loadScene("choose-registration-type.fxml");
    }


}