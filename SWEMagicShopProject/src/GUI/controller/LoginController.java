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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;

// TODO complete this class to manage exceptions in GUI

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private ImageView shopIcon;
    private AccountManager accountManager = new AccountManager();

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResource("/images/shop-icon.png").toExternalForm());
        shopIcon.setImage(image);
    }

    @FXML
    private void handleLogin() {
        // TODO not completed: manage exceptions from accountmanager
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Insert both your email and password to login.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errorLabel.setText("Invalid email entered.");
            return;
        }

        try {
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
                errorLabel.setText("Email or password doesn't match!"); //TODO with exceptions
            }
        } catch (RuntimeException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("An unexpected error happened. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        loadScene("/GUI/view/main-view.fxml");
    }

    private void loadScene(String fxmlFile) {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
