package GUI.controller;

import BusinessLogic.AccountManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ManagerRegistrationController {

    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private ImageView shopIcon;
    @FXML private ImageView penIcon;
    @FXML private ImageView penIcon2;
    @FXML private ImageView emailIcon;
    @FXML private ImageView passwordIcon;

    private AccountManager accountManager;

    public ManagerRegistrationController() {
        accountManager = new AccountManager();
    }

    @FXML
    public void initialize() {
        // Load images
        Image pen = new Image(getClass().getResource("/images/penIcon.png").toExternalForm());
        penIcon.setImage(pen);
        penIcon2.setImage(pen);
        shopIcon.setImage(new Image(getClass().getResource("/images/shop-icon.png").toExternalForm()));
        emailIcon.setImage(new Image(getClass().getResource("/images/emailIcon.png").toExternalForm()));
        passwordIcon.setImage(new Image(getClass().getResource("/images/passwordIcon.png").toExternalForm()));
    }

    @FXML
    private void handleRegistration() {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        try {
            accountManager.createManagerAccount(name, surname, email, password);
            showConfirmation();
        } catch (RuntimeException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception ex) {
            errorLabel.setText("An unexpected error happened. Please try again.");
            ex.printStackTrace();
        }
    }

    private void showConfirmation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/popup.fxml"));
            Parent root = loader.load();

            PopupController popupController = loader.getController();
            popupController.setPopupContent("Account Created", "Your account has been created successfully.", "Back to welcome page");

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            popupStage.setScene(scene);
            popupStage.setResizable(false);
            popupStage.setTitle("Account Created");
            popupStage.showAndWait();
            SceneController.loadScene("welcome-view.fxml");

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Manager account created successfully.");
    }

    @FXML
    private void handleBack() {
        SceneController.loadScene("choose-registration-type.fxml");
    }
}