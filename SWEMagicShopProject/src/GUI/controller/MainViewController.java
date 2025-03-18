package GUI.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
        loadScene("/GUI/view/login-view.fxml");
    }

    @FXML
    private void handleRegisterButton() {
        loadScene(""); //TODO
    }

    private void loadScene(String fxmlFile) {
        try {
            Stage stage = (Stage) iconImageView.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}