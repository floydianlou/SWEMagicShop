package GUI.controller;

import BusinessLogic.AccountManager;
import DomainModel.Customer;
import DomainModel.Manager;
import DomainModel.Person;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

// TODO complete this class to manage exceptions in GUI

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    private AccountManager accountManager = new AccountManager();

    @FXML
    private void handleLogin() {
        // TODO not completed: manage exceptions from accountmanager
        String email = emailField.getText();
        String password = passwordField.getText();

        Person loggedUser = accountManager.login(email, password);

        if (loggedUser != null) {
            System.out.println("Login riuscito! 🎉 Benvenuto, " + loggedUser.getName()); //TODO this is only on console not GUI

            if (loggedUser instanceof Manager) {
                System.out.println("Accesso come Manager.");
                loadScene("/views/manager-shop-view.fxml"); //TODO
            } else if (loggedUser instanceof Customer) {
                System.out.println("Accesso come Cliente.");
                loadScene("/views/customer-shop-view.fxml"); //TODO
            }
        } else {
            errorLabel.setText("Credenziali errate! Riprova."); //TODO with exceptions
        }
    }

    @FXML
    private void handleBack() {
        loadScene("/GUI/view/main-view.fxml");
    }

    private void loadScene(String fxmlFile) {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
