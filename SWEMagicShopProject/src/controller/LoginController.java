package controller;

import BusinessLogic.AccountManager;
import DomainModel.Customer;
import DomainModel.Manager;
import DomainModel.Person;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private AccountManager accountManager;

    public LoginController() {
        this.accountManager = new AccountManager();
    }

    @FXML
    private void handleLoginButtonClick(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Errore", "Inserisci sia email che password!");
            return;
        }

        Person person = accountManager.login(email, password);

        if (person == null) {
            showAlert("Login Fallito", "Email o password non validi.");
        } else if (person instanceof Customer) {
            showAlert("Benvenuto Cliente!", "Accesso riuscito!");
            openDashboard(event, "/gui/views/customer_dashboard.fxml");
        } else if (person instanceof Manager) {
            showAlert("Benvenuto Manager!", "Accesso riuscito!");
            openDashboard(event, "/gui/views/manager_dashboard.fxml");
        }
    }

    @FXML
    private void handleBackToMenuClick(ActionEvent event) {
        openDashboard(event, "/gui/views/main_menu.fxml");
    }

    private void openDashboard(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}


