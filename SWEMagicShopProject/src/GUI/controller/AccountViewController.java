package GUI.controller;

import DomainModel.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AccountViewController {

    private Customer loggedCustomer;

    @FXML private Label accountName;
    @FXML private Label accountSurname;
    @FXML private Label accountEmail;
    @FXML private Label accountPhone;
    @FXML private PasswordField accountPasswordField;
    @FXML private TextField accountPassword;
    @FXML private Label accountAge;
    @FXML private Label accountSpecies;

    @FXML private ImageView orderIcon;
    @FXML private ImageView walletIcon;
    @FXML private ImageView inventoryIcon;
    @FXML private ImageView arcaneIcon;
    @FXML private ImageView eyeIcon;
    @FXML private Image openEyeIcon;
    @FXML private Image closedEyeIcon;

    @FXML private Button togglePasswordButton;

    public void initialize(){
        loggedCustomer = (Customer) LoggedUserManager.getInstance().getLoggedUser();
        loadCustomer();
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
        accountSurname.setText(loggedCustomer.getSurname());
        accountEmail.setText(loggedCustomer.getEmail());
        accountPhone.setText(loggedCustomer.getPhoneNumber());
        accountPassword.setText(loggedCustomer.getPassword());
        accountPasswordField.setText(loggedCustomer.getPassword());
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
