package GUI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class PopupController {
    @FXML
    private Label titleLabel;
    @FXML
    private TextFlow messageLabel;
    @FXML private Button closeButton;
    public MainViewController mainViewController;
    boolean toolbarNeeded = false;

    public void setPopupContent(String title, String message, String buttonText) {
        titleLabel.setText(title);
        closeButton.setText(buttonText);

        Text text = new Text(message);
        text.getStyleClass().add("popup-message-text");
        messageLabel.getChildren().setAll(text);

    }

    public void setMainViewController(MainViewController controller) {
        this.mainViewController = controller;
        toolbarNeeded = true;
    }

    @FXML
    private void handleClosePopup() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();

        if (toolbarNeeded) {
            if (mainViewController != null) {
                mainViewController.loadContent("customer-shop-view.fxml");
                mainViewController.updateTopBar("customer");
            }
            toolbarNeeded = false; // set to false to be safe, shouldn't be necessary since we load it everytime.
        }

    }

}