package GUI.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PopupController {
    @FXML
    private Label titleLabel;
    @FXML
    private Label messageLabel;

    public void setPopupContent(String title, String message) {
        titleLabel.setText(title);
        messageLabel.setText(message);
    }

    @FXML
    private void handleClosePopup() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}