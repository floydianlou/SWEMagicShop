package GUI.controller;

import BusinessLogic.Utilities;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ManagerAuthenticationController {

    @FXML
    private PasswordField authPasswordField;
    @FXML
    private Label errorLabel;
    @FXML
    private ImageView shopIcon;
    @FXML
    private ImageView shopIcon2;

    @FXML
    public void initialize() {
        Image icon = new Image(getClass().getResource("/images/shop-icon.png").toExternalForm());
        shopIcon.setImage(icon);
        shopIcon2.setImage(icon);

        authPasswordField.setOnAction(e -> handleAuth());
    }

    @FXML
    private void handleAuth() {
        String authPassword = authPasswordField.getText();

        if (authPassword.isEmpty()) {
            errorLabel.setText("Please enter the authentication password.");
            return;
        }

        try {
            if (Utilities.validManagerPassword(authPassword)) {
                SceneController.loadScene("manager-registration-view.fxml");
            } else {
                errorLabel.setText("Invalid authentication password.");
            }
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        } catch (RuntimeException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception ex) {
            errorLabel.setText("An unexpected error happened. Please try again.");
            ex.printStackTrace();
        }
    }
    @FXML
    private void handleBack () {
        SceneController.loadScene("choose-registration-type.fxml");
    }
}