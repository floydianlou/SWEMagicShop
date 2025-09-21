package GUI.controller;

import BusinessLogic.AccountManager;
import BusinessLogic.Utilities;
import DomainModel.ArcaneRequest;
import BusinessLogic.ArcaneRequestManager;

import DomainModel.Customer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ManagerArcanePopup {

    @FXML private Button closeButton;
    @FXML private Button approveButton;
    @FXML private Button rejectButton;
    @FXML private Button backButton;

    @FXML private Label titleLabel;
    @FXML private Label requestID;
    @FXML private Label customerNameLabel;
    @FXML private Label customerIdLabel;
    @FXML private Label statusLabel;
    @FXML private Label dateLabel;
    @FXML private Label customerName;
    @FXML private Label customerPhone;
    @FXML private Label customerEmail;
    @FXML private Label customerSpecies;
    @FXML private Label customerAge;

    private Stage stage;
    private ArcaneRequest request;
    private AccountManager accountManager = new AccountManager();
    private ArcaneRequestManager arcaneManager = new ArcaneRequestManager();

    private Consumer<Boolean> onResult;
    public void setOnResult(Consumer<Boolean> onResult) { this.onResult = onResult; }

    public void setStage(Stage stage) { this.stage = stage; }

    public void setRequest(ArcaneRequest request) {
        this.request = request;
        titleLabel.setText("Arcane Request Details and Actions");
        backButton.setText("Back to requests");

        requestID.setText("#" + request.getRequestID());
        statusLabel.setText(request.getRequestStatus());
        dateLabel.setText(Utilities.formatOrderDate(request.getRequestDate()));

        boolean isPending = false;
        if (request.getRequestStatus().equals("Pending")) {
            isPending = true;
        }
        approveButton.setVisible(isPending);
        rejectButton.setVisible(isPending);
        approveButton.setManaged(isPending);
        rejectButton.setManaged(isPending);

        loadCustomerDetails(request.getCustomerID());
    }

    @FXML
    private void handleApprove() {
        boolean approved = false;
        try {
            approved = arcaneManager.approveRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (onResult != null) onResult.accept(approved);
        if (stage != null) stage.close();
    }

    @FXML
    private void handleReject() {
        boolean rejected = false;
        try {
            rejected = arcaneManager.rejectRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (onResult != null) onResult.accept(rejected);
        if (stage != null) stage.close();
    }

    @FXML
    private void handleClosePopup() {
        if (stage != null) stage.close();
    }

    private void loadCustomerDetails(int customerId) {
        Customer customer = accountManager.getCustomerByID(customerId);

        customerName.setText(customer.getName() + " " + customer.getSurname());
        customerEmail.setText(customer.getEmail());
        customerPhone.setText(customer.getPhoneNumber());
        customerSpecies.setText(customer.getSpeciesName());
        customerAge.setText(customer.getAge() + " years");
    }

    public static Stage openPopup(Parent root, Node ownerForPos) {
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        Stage popup = new Stage(StageStyle.TRANSPARENT);
        popup.initModality(Modality.APPLICATION_MODAL);
        if (ownerForPos != null) popup.initOwner(ownerForPos.getScene().getWindow());
        popup.setScene(scene);

        Node card = root.lookup(".popup-card");
        final double[] d = new double[2];
        if (card != null) {
            card.setOnMousePressed(e -> { d[0] = e.getScreenX() - popup.getX(); d[1] = e.getScreenY() - popup.getY(); });
            card.setOnMouseDragged(e -> { popup.setX(e.getScreenX() - d[0]); popup.setY(e.getScreenY() - d[1]); });
        }
        popup.show();
        return popup;
    }
}