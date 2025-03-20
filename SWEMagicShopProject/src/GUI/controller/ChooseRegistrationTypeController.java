package GUI.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ChooseRegistrationTypeController {

    @FXML
    private void handleClientRegistration(ActionEvent event) {
        System.out.println("handleClientRegistration invoked");
        loadScene("/GUI/view/user-registration.fxml", event);
    }

    @FXML
    private void handleManagerRegistration(ActionEvent event) {
        System.out.println("handleManagerRegistration invoked");
        // Per ora, reindirizza alla registrazione utente (da modificare quando hai la pagina manager)
        loadScene("/GUI/view/user-registration.fxml", event);
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        System.out.println("handleBackToMenu invoked");
        loadScene("/GUI/view/main-view.fxml", event);
    }

    private void loadScene(String fxmlFile, ActionEvent event) {
        System.out.println("Tentativo di caricare: " + fxmlFile);
        if(getClass().getResource(fxmlFile) == null) {
            System.out.println("Resource non trovata: " + fxmlFile);
            return;
        }
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            System.out.println("Scena caricata con successo: " + fxmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


