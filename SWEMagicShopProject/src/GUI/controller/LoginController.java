package GUI.controller;

import BusinessLogic.AccountManager;
import BusinessLogic.CartManager;
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
    @FXML private Label password;
    @FXML private Label errorLabel;
    @FXML private ImageView shopIcon;
    @FXML private ImageView eyeIcon;
    @FXML private Image openEyeIcon;
    @FXML private Image closedEyeIcon;
    private AccountManager accountManager = new AccountManager();

    @FXML
    public void initialize() {
        shopIcon.setImage(new Image(getClass().getResource("/images/shop-icon.png").toExternalForm()));
        emailIcon.setImage(new Image(getClass().getResource("/images/emailIcon.png").toExternalForm()));
        passwordIcon.setImage(new Image(getClass().getResource("/images/passwordIcon.png").toExternalForm()));
        openEyeIcon = new Image(getClass().getResource("/images/openEyeIcon.png").toExternalForm());
        closedEyeIcon = new Image(getClass().getResource("/images/closedEyeIcon.png").toExternalForm());
        eyeIcon.setImage(closedEyeIcon);
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
            LoggedUserManager.getInstance().setLoggedUser(loggedUser);
            if (loggedUser != null) {
                if (loggedUser instanceof Customer) {
                    CartManager.init((Customer) loggedUser); }
                    SceneController.loadScene("main-view.fxml");
            } else {
                errorLabel.setText("Email or password doesn't match!");
            }
        } catch (RuntimeException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("An unexpected error happened. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    private void togglePassword(){
        if (passwordField.isVisible()) {
            password.setText(passwordField.getText());
            password.setVisible(true);
            passwordField.setVisible(false);
            eyeIcon.setImage(openEyeIcon);
        } else {
            passwordField.setText(password.getText());
            passwordField.setVisible(true);
            password.setVisible(false);
            eyeIcon.setImage(closedEyeIcon);
        }
    }

    @FXML
    private void handleBack() {
        SceneController.loadScene("welcome-view.fxml");
}
}