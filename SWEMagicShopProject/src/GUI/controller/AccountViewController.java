package GUI.controller;

import BusinessLogic.AccountManager;
import DomainModel.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

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
        boolean isEditing = accountName.isEditable(); // accountName not edible then isEditing is false

        accountName.setEditable(!isEditing);  //therefore i make everything edible
        accountSurname.setEditable(!isEditing);
        accountEmail.setEditable(!isEditing);
        accountPhone.setEditable(!isEditing);
        accountPassword.setEditable(!isEditing);
        accountPassword.setVisible(!isEditing); //i make visible the password field and eyeicon
        accountPasswordField.setVisible(isEditing); //i make invisible the password field and eyeicon
        eyeIcon.setVisible(isEditing);

        edit.setVisible(false);
        confirmEdit.setVisible(true);
        cancelEdit.setVisible(true);

    }

    @FXML
    private void editAccount(){
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
            if(accountManager.updateCustomerAccount(updatedCustomer)){
                LoggedUserManager.getInstance().setLoggedUser(updatedCustomer);
                loggedCustomer = (Customer) LoggedUserManager.getInstance().getLoggedUser();
            }
        } catch (RuntimeException e) {
            errorLabel.setText(e.getMessage());
            cancelEdit();
        }

        loadCustomer();
        resetEditButton();
    }

    @FXML
    private void cancelEdit() {
        loadCustomer();
        resetEditButton();
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
