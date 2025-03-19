package GUI.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import BusinessLogic.AccountManager;
import DomainModel.Species;
import javafx.stage.Stage;

import java.io.IOException;

public class UserRegistrationController {

    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField ageField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> speciesComboBox;
    @FXML private Button registerButton;

    private AccountManager accountManager;

    public UserRegistrationController() {
        // Inizializziamo l'AccountManager
        accountManager = new AccountManager();
    }

    @FXML
    public void initialize() {
        // Popoliamo la ComboBox con alcuni valori
        speciesComboBox.getItems().addAll("Human", "Elf", "Dwarf", "Orc");
        speciesComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleRegistration(ActionEvent event) {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String ageText = ageField.getText().trim();
        String phone = phoneField.getText().trim();
        String speciesStr = speciesComboBox.getSelectionModel().getSelectedItem();

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid age", "Please enter a valid number for age.");
            return;
        }

        // Creiamo un oggetto Species (valori d'esempio: ID=1, legalAge=18, limitAge=100)
        Species species = new Species(1, speciesStr, 18, 100);

        try {
            accountManager.createCustomerAccount(name, surname, email, password, age, phone, species);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration Completed", "Your account has been created successfully.");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Error creating account", ex.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadScene(String fxmlFile) {
        try {
            Stage stage = (Stage) registerButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

