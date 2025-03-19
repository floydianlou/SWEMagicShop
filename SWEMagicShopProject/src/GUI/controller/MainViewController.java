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


    @FXML
    private void goToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/UserRegistration.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // 1) Recuperiamo lo stage dalla sorgente dell'evento
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // 2) Impostiamo la nuova scena
            stage.setScene(scene);
            stage.setTitle("Otari's Magic Shop - User Registration");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}