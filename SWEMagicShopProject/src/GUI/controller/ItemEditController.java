package GUI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class ItemEditController {

    @FXML private TextField itemNameField;
    @FXML private TextField itemPriceField;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        // Initialize the view, load item details, etc.
    }

    @FXML
    private void handleSave() {
        // Implement save functionality
        String itemName = itemNameField.getText();
        String itemPrice = itemPriceField.getText();
        System.out.println("Saving item: " + itemName + " with price: " + itemPrice);
    }

    @FXML
    private void handleCancel() {
        // Navigate back to the manager shop view
        SceneController.loadScene("manager-shop-view.fxml");
    }
}