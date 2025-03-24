package GUI.controller;

import BusinessLogic.AccountManager;
import DomainModel.Customer;
import DomainModel.Manager;
import DomainModel.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// TODO complete this class to manage exceptions in GUI

public class LoginController {

    @FXML private TextField emailField;
    @FXML private ImageView emailIcon;
    @FXML private ImageView passwordIcon;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private ImageView shopIcon;
    private AccountManager accountManager = new AccountManager();

    @FXML
    public void initialize() {
        shopIcon.setImage(new Image(getClass().getResource("/images/shop-icon.png").toExternalForm()));
        emailIcon.setImage(new Image(getClass().getResource("/images/emailIcon.png").toExternalForm()));
        passwordIcon.setImage(new Image(getClass().getResource("/images/passwordIcon.png").toExternalForm()));
    }

    @FXML
    private void handleLogin() {
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
                System.out.println("Login completed."); //TODO this is only on console not GUI

                if (loggedUser instanceof Manager) {
                    System.out.println("Accesso come Manager.");
                    SceneController.loadScene("manager-shop-view.fxml"); //TODO
                } else if (loggedUser instanceof Customer) {
                    SceneController.loadSceneWithCustomer("customer-shop-view.fxml", (Customer) loggedUser);
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
        SceneController.loadScene("main-view.fxml");
    }
}
