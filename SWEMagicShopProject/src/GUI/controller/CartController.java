package GUI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import DomainModel.Customer;
import DomainModel.Person;

public class CartController {

    @FXML
    private Label cartLabel;

    @FXML
    public void initialize() {
        Person user = LoggedUserManager.getInstance().getLoggedUser();

        if (user instanceof Customer customer) {
            cartLabel.setText("Carrello di " + customer.getName());
        } else {
            cartLabel.setText("Carrello (utente non riconosciuto)");
        }
    }
}