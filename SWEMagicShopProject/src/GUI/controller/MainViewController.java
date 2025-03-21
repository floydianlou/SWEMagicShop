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

// TODO complete with register page actions

public class MainViewController {

    @FXML
    private ImageView iconImageView;

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