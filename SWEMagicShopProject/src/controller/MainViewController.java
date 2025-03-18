package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainViewController {

    @FXML
    private Label label;

    @FXML
    private Button button;

    @FXML
    private void handleButtonClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pagina di Login");
        alert.setHeaderText(null);
        alert.setContentText("Sezione Login");
        alert.showAndWait();
    }
}