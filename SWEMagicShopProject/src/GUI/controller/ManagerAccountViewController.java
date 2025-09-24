package GUI.controller;

import BusinessLogic.AccountManager;
import DomainModel.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.SQLException;

public class ManagerAccountViewController {

    private Manager loggedManager;
    private MainViewController mainViewController;
    private AccountManager accountManager;

    @FXML private TextField accountName;
    @FXML private TextField accountSurname;
    @FXML private TextField accountEmail;
    @FXML private PasswordField accountPasswordField;
    @FXML private TextField accountPassword;

    @FXML private Label errorLabel;

    @FXML private ImageView orderIcon;
    @FXML private ImageView arcaneIcon;

    @FXML private ImageView eyeIcon;
    @FXML private Image openEyeIcon;
    @FXML private Image closedEyeIcon;
    @FXML private ImageView penIcon1;
    @FXML private ImageView penIcon2;
    @FXML private ImageView penIcon3;
    @FXML private ImageView penIcon4;

    @FXML private Button cancelEdit;
    @FXML private Button confirmEdit;
    @FXML private Button edit;

    @FXML
    public void initialize(){
        loggedManager = (Manager) LoggedUserManager.getInstance().getLoggedUser();
        loadManager();
        accountManager = new AccountManager();
        orderIcon.setImage(new Image(getClass().getResource("/images/ordersIcon.png").toExternalForm()));
        arcaneIcon.setImage(new Image(getClass().getResource("/images/arcaneIcon.png").toExternalForm()));
        openEyeIcon = new Image(getClass().getResource("/images/openEyeIcon.png").toExternalForm());
        closedEyeIcon = new Image(getClass().getResource("/images/closedEyeIcon.png").toExternalForm());
        eyeIcon.setImage(closedEyeIcon);
        penIcon1.setImage(new Image(getClass().getResource("/images/penIcon.png").toExternalForm()));
        penIcon2.setImage(new Image(getClass().getResource("/images/penIcon.png").toExternalForm()));
        penIcon3.setImage(new Image(getClass().getResource("/images/penIcon.png").toExternalForm()));
        penIcon4.setImage(new Image(getClass().getResource("/images/penIcon.png").toExternalForm()));
    }

    public void loadManager(){
        accountName.setText(loggedManager.getName());
        accountName.setEditable(false);
        accountSurname.setText(loggedManager.getSurname());
        accountSurname.setEditable(false);
        accountEmail.setText(loggedManager.getEmail());
        accountEmail.setEditable(false);
        accountPassword.setText(loggedManager.getPassword());
        accountPassword.setEditable(false);
        accountPasswordField.setText(loggedManager.getPassword());
        accountPasswordField.setEditable(false);
    }

    @FXML
    private void togglePassword(){
        if (accountPasswordField.isVisible()) {
            accountPassword.setText(accountPasswordField.getText());
            accountPassword.setVisible(true);
            accountPasswordField.setVisible(false);
            eyeIcon.setImage(openEyeIcon);
        } else {
            accountPasswordField.setText(accountPassword.getText());
            accountPasswordField.setVisible(true);
            accountPassword.setVisible(false);
            eyeIcon.setImage(closedEyeIcon);
        }
    }

    @FXML
    private void handleEditAccount() {
        accountName.setEditable(true);
        accountSurname.setEditable(true);
        accountEmail.setEditable(true);
        accountPassword.setEditable(true);
        accountPassword.setVisible(true);
        accountPasswordField.setVisible(false);
        eyeIcon.setVisible(false);

        edit.setVisible(false);
        penIcon1.setVisible(true);
        penIcon2.setVisible(true);
        penIcon3.setVisible(true);
        penIcon4.setVisible(true);
        confirmEdit.setVisible(true);
        cancelEdit.setVisible(true);

    }

    @FXML
    private void editAccount() {
        errorLabel.setText("");

        if (accountName.getText().isEmpty() || accountSurname.getText().isEmpty()
                || accountEmail.getText().isEmpty() || accountPassword.getText().isEmpty()) {
            errorLabel.setText("Please fill all the fields");
            return;
        }

        Manager updated = new Manager(loggedManager.getPersonID());
        updated.setName(accountName.getText().trim());
        updated.setSurname(accountSurname.getText().trim());
        updated.setEmail(accountEmail.getText().trim());
        updated.setPassword(accountPassword.getText());

        try {
            accountManager.updateManagerAccount(updated);
            LoggedUserManager.getInstance().setLoggedUser(updated);
            loggedManager = (Manager) LoggedUserManager.getInstance().getLoggedUser();

            popupAccountEdited();
            loadManager();
            resetEditButton();

        } catch (SQLException e) {
            errorLabel.setText(e.getMessage());
        } catch (IllegalArgumentException badInput) {
            errorLabel.setText(badInput.getMessage());
        } catch (Exception ex) {
            errorLabel.setText("Unexpected error. Please try again.");
            ex.printStackTrace();
        }
    }

    @FXML
    private void cancelEdit() {
        loadManager();
        resetEditButton();
        errorLabel.setText("");
    }

    private void resetEditButton(){
        edit.setVisible(true);
        confirmEdit.setVisible(false);
        cancelEdit.setVisible(false);
        accountName.setEditable(false);
        accountSurname.setEditable(false);
        accountEmail.setEditable(false);
        accountPassword.setEditable(false);
        accountPasswordField.setVisible(true);
        accountPassword.setVisible(false);
        eyeIcon.setVisible(true);
        penIcon1.setVisible(false);
        penIcon2.setVisible(false);
        penIcon3.setVisible(false);
        penIcon4.setVisible(false);
    }

    private void popupAccountEdited() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/popup.fxml"));
            Parent root = loader.load();

            PopupController popupController = loader.getController();
            popupController.setPopupContent("Account Updated", "Your account has been updated successfully.", "OK");

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.setTitle("Account Updated");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            popupStage.setScene(scene);
            popupStage.setResizable(false);
            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void goToStatistics(){
        mainViewController.loadContent("manager-statistics-view.fxml");
        mainViewController.updateTopBar("manageStatistics");
    }


    @FXML
    private void goToManageArcaneRequest() {
        mainViewController.loadContent("manager-arcane-view.fxml");
        mainViewController.updateTopBar("manageArcaneRequest");
    }

    public void setMainViewController(MainViewController controller) {
        this.mainViewController = controller;
    }
}
