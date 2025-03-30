package GUI.controller;

import BusinessLogic.AccountManager;
import DomainModel.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AccountViewController {

    private Customer loggedCustomer;

    @FXML private TextField accountName;
    @FXML private TextField accountSurname;
    @FXML private TextField accountEmail;
    @FXML private TextField accountPhone;
    @FXML private PasswordField accountPasswordField;
    @FXML private TextField accountPassword;
    @FXML private Label accountAge;
    @FXML private Label accountSpecies;

    @FXML private Label errorLabel;

    @FXML private ImageView orderIcon;
    @FXML private ImageView walletIcon;
    @FXML private ImageView inventoryIcon;
    @FXML private ImageView arcaneIcon;
    @FXML private ImageView eyeIcon;
    @FXML private Image openEyeIcon;
    @FXML private Image closedEyeIcon;
    @FXML private ImageView penIcon1;
    @FXML private ImageView penIcon2;
    @FXML private ImageView penIcon3;
    @FXML private ImageView penIcon4;
    @FXML private ImageView penIcon5;

    @FXML private Button cancelEdit;
    @FXML private Button confirmEdit;
    @FXML private Button edit;

    private AccountManager accountManager;

    public void initialize(){
        loggedCustomer = (Customer) LoggedUserManager.getInstance().getLoggedUser();
        loadCustomer();
        accountManager = new AccountManager();
        orderIcon.setImage(new Image(getClass().getResource("/images/ordersIcon.png").toExternalForm()));
        walletIcon.setImage(new Image(getClass().getResource("/images/walletIcon.png").toExternalForm()));
        inventoryIcon.setImage(new Image(getClass().getResource("/images/inventoryIcon.png").toExternalForm()));
        arcaneIcon.setImage(new Image(getClass().getResource("/images/arcaneIcon.png").toExternalForm()));
        openEyeIcon = new Image(getClass().getResource("/images/openEyeIcon.png").toExternalForm());
        closedEyeIcon = new Image(getClass().getResource("/images/closedEyeIcon.png").toExternalForm());
        eyeIcon.setImage(closedEyeIcon);
        penIcon1.setImage(new Image(getClass().getResource("/images/penIcon.png").toExternalForm()));
        penIcon2.setImage(new Image(getClass().getResource("/images/penIcon.png").toExternalForm()));
        penIcon3.setImage(new Image(getClass().getResource("/images/penIcon.png").toExternalForm()));
        penIcon4.setImage(new Image(getClass().getResource("/images/penIcon.png").toExternalForm()));
        penIcon5.setImage(new Image(getClass().getResource("/images/penIcon.png").toExternalForm()));
    }

    public void loadCustomer(){
        accountName.setText(loggedCustomer.getName());
        accountName.setEditable(false);
        accountSurname.setText(loggedCustomer.getSurname());
        accountSurname.setEditable(false);
        accountEmail.setText(loggedCustomer.getEmail());
        accountEmail.setEditable(false);
        accountPhone.setText(loggedCustomer.getPhoneNumber());
        accountPhone.setEditable(false);
        accountPassword.setText(loggedCustomer.getPassword());
        accountPassword.setEditable(false);
        accountPasswordField.setText(loggedCustomer.getPassword());
        accountPasswordField.setEditable(false);
        accountAge.setText(String.valueOf(loggedCustomer.getAge()));
        accountSpecies.setText(loggedCustomer.getSpeciesName());
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
        accountName.setEditable(true);  //therefore i make everything edible
        accountSurname.setEditable(true);
        accountEmail.setEditable(true);
        accountPhone.setEditable(true);
        accountPassword.setEditable(true);
        accountPassword.setVisible(true); //i make visible the password text field
        accountPasswordField.setVisible(false); //i make invisible the password field and eyeicon
        eyeIcon.setVisible(false);

        edit.setVisible(false);
        penIcon1.setVisible(true);
        penIcon2.setVisible(true);
        penIcon3.setVisible(true);
        penIcon4.setVisible(true);
        penIcon5.setVisible(true);
        confirmEdit.setVisible(true);
        cancelEdit.setVisible(true);

    }

    @FXML
    private void editAccount(){
        errorLabel.setText("");
        Customer updatedCustomer = new Customer(loggedCustomer.getPersonID());
        if(accountName.getText().isEmpty() || accountSurname.getText().isEmpty() || accountEmail.getText().isEmpty() || accountPhone.getText().isEmpty() || accountPassword.getText().isEmpty()){
            errorLabel.setText("Please fill all the fields");
            return;
        }
        updatedCustomer.setName(accountName.getText());
        updatedCustomer.setSurname(accountSurname.getText());
        updatedCustomer.setEmail(accountEmail.getText());
        updatedCustomer.setPhoneNumber(accountPhone.getText());
        updatedCustomer.setPassword(accountPassword.getText());
        updatedCustomer.setAge(loggedCustomer.getAge());
        updatedCustomer.setOwnSpecies(loggedCustomer.getOwnSpecies());
        try{
            accountManager.updateCustomerAccount(updatedCustomer);
            LoggedUserManager.getInstance().setLoggedUser(updatedCustomer);
            loggedCustomer = (Customer) LoggedUserManager.getInstance().getLoggedUser();
            popupAccountEdited();

        } catch (RuntimeException e) {
            errorLabel.setText(e.getMessage());
        }
        loadCustomer();
        resetEditButton();
    }

    @FXML
    private void cancelEdit() {
        loadCustomer();
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
        accountPhone.setEditable(false);
        accountPassword.setEditable(false);
        accountPasswordField.setVisible(true);
        accountPassword.setVisible(false);
        eyeIcon.setVisible(true);
        penIcon1.setVisible(false);
        penIcon2.setVisible(false);
        penIcon3.setVisible(false);
        penIcon4.setVisible(false);
        penIcon5.setVisible(false);
    }

    private void popupAccountEdited() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/popup.fxml"));
            Parent root = loader.load();

            PopupController popupController = loader.getController();
            popupController.setPopupContent("Account Updated", "Your account has been updated successfully.");

            Stage popupStage = new Stage();
            popupStage.setTitle("Account Updated");
            popupStage.setScene(new Scene(root));
            popupStage.setResizable(false);
            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToOrders(){
        SceneController.loadScene("orders-view.fxml");
    }

    @FXML
    private void goToWallet(){
        SceneController.loadScene("wallet-view.fxml");
    }

    @FXML
    private void goToInventory(){
        SceneController.loadScene("inventory-view.fxml");
    }

    @FXML
    private void goToArcaneRequest(){
        SceneController.loadScene("arcane-request-view.fxml");
    }

}
