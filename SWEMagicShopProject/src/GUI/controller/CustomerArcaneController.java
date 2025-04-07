package GUI.controller;

import BusinessLogic.ArcaneRequestManager;
import BusinessLogic.CustomerOrderManager;
import DomainModel.ArcaneRequest;
import DomainModel.Customer;
import DomainModel.Order;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class CustomerArcaneController {

    private ArcaneRequestManager arcaneRequestManager = new ArcaneRequestManager();

    @FXML
    private VBox contentBox;
    @FXML
    private ImageView arcaneItem;
    @FXML
    private ImageView reversedArcaneItem;
    @FXML
    private Customer currentCustomer;

    @FXML private VBox answerBox1;
    @FXML private VBox answerBox2;
    @FXML private VBox answerBox3;

    @FXML private Button toggle1;
    @FXML private Button toggle2;
    @FXML private Button toggle3;


    @FXML
    public void initialize() {
        currentCustomer = (Customer) LoggedUserManager.getInstance().getLoggedUser();
        arcaneItem.setImage(new Image(getClass().getResource("/images/arcaneItem.png").toExternalForm()));
        reversedArcaneItem.setImage(new Image(getClass().getResource("/images/arcaneItemReversed.png").toExternalForm()));

        displayArcaneInfo();
    }

    private void displayArcaneInfo() {
        ArrayList<ArcaneRequest> requests = arcaneRequestManager.viewRequestsByCustomer(currentCustomer.getPersonID());

        if (requests.isEmpty()) {
            // customer has never made an arcane request and gets a special view
            contentBox.getChildren().setAll(newMemberForm());
            return;
        }

        ArcaneRequest latestRequest = requests.get(0); // since DAO has order by latest request is always on top
        boolean requestsCap = requests.size() >= 5;
        boolean lastRejected = latestRequest.getRequestStatus().equals("Rejected");

        // if customer can't make any more requests they get a special view
        if (requestsCap && lastRejected) {
            contentBox.getChildren().setAll(endedRequestsForm());
            return;
        }

        switch (latestRequest.getRequestStatus()) {
            case "Pending" -> contentBox.getChildren().setAll(pendingRequestForm());
            case "Approved" -> contentBox.getChildren().setAll(arcaneMemberForm());
            case "Rejected" -> contentBox.getChildren().setAll(newMemberForm());
        }

    }

    private Node newMemberForm() {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);

        Label label = new Label("Want to have an Arcane Membership?");
        Button sendButton = new Button("Submit your request");
        sendButton.setOnAction(e -> handleRequestSubmission());

        box.getChildren().addAll(label, sendButton);
        return box;
    }

    private Node pendingRequestForm() {
        Label label = new Label("Your request is being reviewed by one of our shop managers.");
        label.setWrapText(true);
        return label;
    }

    private Node arcaneMemberForm() {
        Label label = new Label("You already are an Arcane Member. Enjoy all things weird and obscure in our shop.");
        label.setWrapText(true);
        return label;
    }

    private Node endedRequestsForm() {
        Label label = new Label("Your requests have been refused too many times. You're banned from getting an Arcane Membership.");
        label.setWrapText(true);
        label.setStyle("-fx-text-fill: #b00020; -fx-font-size: 16px;");
        return label;
    }

    private void handleRequestSubmission() { // TODO make my own popup
        boolean success = arcaneRequestManager.makeArcaneRequest(currentCustomer);
        if (success) {
            showPopup("Request sent successfully.");
            displayArcaneInfo();
        } else {
            showPopup("Can't send a request right now.");
        }
    }

    private void showPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("🌙 Arcane Membership");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void toggleAnswer1() {
        boolean nowVisible = !answerBox1.isVisible();
        answerBox1.setVisible(nowVisible);
        answerBox1.setManaged(nowVisible);
        toggle1.setText(nowVisible ? "−" : "+");
    }

    @FXML
    private void toggleAnswer2() {
        boolean nowVisible = !answerBox2.isVisible();
        answerBox2.setVisible(nowVisible);
        answerBox2.setManaged(nowVisible);
        toggle2.setText(nowVisible ? "−" : "+");
    }

    @FXML
    private void toggleAnswer3() {
        boolean nowVisible = !answerBox3.isVisible();
        answerBox3.setVisible(nowVisible);
        answerBox3.setManaged(nowVisible);
        toggle3.setText(nowVisible ? "−" : "+");
    }

}
