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
    public void handleRegisterChoice(ActionEvent actionEvent) { //to choose type of registration button
        loadScene("/GUI/view/choose-registration-type.fxml");
    }

    @FXML
    private void handleRegisterButton() { //User registration button
        loadScene("/GUI/view/user-registration.fxml");
    }

    private void loadScene(String fxmlFile) {
        System.out.println("Tentativo di caricare: " + fxmlFile);
        if(getClass().getResource(fxmlFile) == null) {
            System.out.println("Resource non trovata: " + fxmlFile);
            return;
        }
        try {
            Stage stage = (Stage) iconImageView.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            System.out.println("Scena caricata con successo.");
        } catch (IOException e) {
            System.out.println("Eccezione durante il caricamento:");
            e.printStackTrace();
        }
    }


}