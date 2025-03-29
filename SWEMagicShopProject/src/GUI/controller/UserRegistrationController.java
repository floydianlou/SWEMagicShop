package GUI.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import BusinessLogic.AccountManager;
import DomainModel.Species;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class UserRegistrationController {

    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField ageField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> speciesDropdown;

    // images

    @FXML private ImageView penIcon;
    @FXML private ImageView penIcon2;
    @FXML private ImageView emailIcon;
    @FXML private ImageView passwordIcon;
    @FXML private ImageView speciesIcon;
    @FXML private ImageView ageIcon;
    @FXML private ImageView phoneIcon;
    @FXML private Button registerButton;
    @FXML private ImageView shopIcon;
    @FXML private Label errorLabel;
    private ArrayList<Species> allSpecies;

    private AccountManager accountManager;

    public UserRegistrationController() {
        accountManager = new AccountManager();
    }

    @FXML
    public void initialize() {
        // IMAGE LOADING
        Image pen = new Image(getClass().getResource("/images/penIcon.png").toExternalForm());
        penIcon.setImage(pen);
        penIcon2.setImage(pen);
        shopIcon.setImage(new Image(getClass().getResource("/images/shop-icon.png").toExternalForm()));
        emailIcon.setImage(new Image(getClass().getResource("/images/emailIcon.png").toExternalForm()));
        passwordIcon.setImage(new Image(getClass().getResource("/images/passwordIcon.png").toExternalForm()));
        speciesIcon.setImage(new Image(getClass().getResource("/images/speciesIcon.png").toExternalForm()));
        ageIcon.setImage(new Image(getClass().getResource("/images/ageIcon.png").toExternalForm()));
        phoneIcon.setImage(new Image(getClass().getResource("/images/phoneIcon.png").toExternalForm()));

        loadSpeciesInDropdown();
        ageField.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("\\d*")) ? change : null));
    }

    public void loadSpeciesInDropdown() {
        try {
        allSpecies = accountManager.getAllSpecies(); }
        catch (RuntimeException e) {
            errorLabel.setText(e.getMessage());
        }
        for (Species species : allSpecies) {
            speciesDropdown.getItems().add(species.getName());
        }
    }

    @FXML
    private void handleRegistration() {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String phone = phoneField.getText();
        String selectedSpeciesName = speciesDropdown.getValue();

        if (email.isEmpty() || password.isEmpty()||name.isEmpty()||surname.isEmpty()||phone.isEmpty()) {
            errorLabel.setText("Insert all your data to create an account");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageField.getText().trim());
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid age. Please enter a valid number.");
            return;
        }

        if (selectedSpeciesName == null) {
            errorLabel.setText("Please select a species.");
            return;
        }

        Species selected = null;
        for (Species species : allSpecies) {
            if (species.getName().equals(selectedSpeciesName)) {
                selected = species;
            }
        }

        try {
            accountManager.createCustomerAccount(name, surname, email, password, age, phone, selected);
            popupAccountComplete();

        }
        catch (RuntimeException e){
            errorLabel.setText(e.getMessage());
        }
        catch (Exception ex) {
            errorLabel.setText("An unexpected error happened. Please try again.");
            ex.printStackTrace();
        }
    }

    private void popupAccountComplete() {
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
    }

    @FXML
    private void handleBack() {
        SceneController.loadScene("choose-registration-type.fxml");
    }

}

