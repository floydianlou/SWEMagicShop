package GUI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class ItemEditController {

    //TODO: DELETE THIS CLASS IF NOT NEEDED
    @FXML private TextField itemNameField;
    @FXML private TextField itemPriceField;
    @FXML private Label errorLabel;
    private MainViewController mainViewController;

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


    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }


}