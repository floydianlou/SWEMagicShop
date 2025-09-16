package GUI.controller;

import BusinessLogic.ArcaneRequestManager;
import BusinessLogic.CustomerOrderManager;
import DomainModel.ArcaneRequest;
import DomainModel.Customer;
import DomainModel.Order;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    @FXML private VBox answerBox4;

    @FXML private Button toggle1;
    @FXML private Button toggle2;
    @FXML private Button toggle3;
    @FXML private Button toggle4;


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
            case "Rejected" -> contentBox.getChildren().setAll(lessThanFiveForm());
        }

    }

    // has requested but requests remaining
    private Node lessThanFiveForm() {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("arcane-form");

        Label label = new Label("You can still request an Arcane Membership");
        label.getStyleClass().add("arcane-form-title");

        Label description = new Label("One of our shop managers will review your request soon.");
        description.setWrapText(true);
        description.setTextAlignment(TextAlignment.CENTER);
        description.setMaxWidth(450);
        description.getStyleClass().add("arcane-form-description");

        Button sendButton = new Button("Submit your Arcane request");
        sendButton.getStyleClass().add("arcane-button");
        sendButton.setOnAction(e -> handleRequestSubmission());

        int totalRequests = arcaneRequestManager.viewNumberOfArcaneRequests(currentCustomer.getPersonID());
        int remaining = 5 - totalRequests;

        Label counterText = new Label("You can still submit " + remaining + " request" + (remaining == 1 ? "." : "s."));
        counterText.getStyleClass().add(remaining == 1 ? "requests-remaining-one" : "requests-remaining");

        HBox counterBox = new HBox(7, counterText);
        counterBox.setAlignment(Pos.CENTER);

        box.getChildren().addAll(label, description, sendButton, counterBox);
        return box;
    }

    // never requested form
    private Node newMemberForm() {
        VBox box = new VBox(30);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("arcane-form");

        Label title = new Label("Request an Arcane Membership here!");
        title.getStyleClass().add("arcane-form-title");

        Label description = new Label("One of our shop managers will review your request soon.");
        description.setWrapText(true);
        description.setTextAlignment(TextAlignment.CENTER);
        description.setMaxWidth(450);
        description.getStyleClass().add("arcane-form-description");

        Button sendButton = new Button("Submit your Arcane Request");
        sendButton.getStyleClass().add("arcane-button");
        sendButton.setOnAction(e -> handleRequestSubmission());

        box.getChildren().addAll(title, description, sendButton);
        return box;
    }

    // pending request form
    private Node pendingRequestForm() {
        VBox box = new VBox(30);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("arcane-form");

        Label title = new Label("Your request has been submitted.");
        title.getStyleClass().add("arcane-form-title");

        Label message = new Label(
                "Your request is currently being examined by one of our shop managers.\n" +
                        "Rest assured you will receive our reply as soon as possible, according to the current moon phase."
        );
        message.setWrapText(true);
        message.setTextAlignment(TextAlignment.CENTER);
        message.setMaxWidth(500);
        message.getStyleClass().add("arcane-form-description");

        ProgressIndicator waiting = new ProgressIndicator();
        waiting.setPrefSize(50, 50);

        box.getChildren().addAll(title, message, waiting);
        return box;
    }

    // already member form
    private Node arcaneMemberForm() {
        VBox box = new VBox(25);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("arcane-form");

        Label title = new Label("You have been accepted as an Arcane Member.");
        title.getStyleClass().add("arcane-form-title");

        Label description = new Label(getRandomDescription());
        description.setWrapText(true);
        description.setTextAlignment(TextAlignment.CENTER);
        description.setMaxWidth(500);
        description.getStyleClass().add("arcane-form-description");

        // icon and glow
        ImageView arcaneIcon = new ImageView(new Image(getClass().getResource("/images/arcaneMemberIcon.png").toExternalForm()));
        arcaneIcon.setFitWidth(150);
        arcaneIcon.setFitHeight(150);
        arcaneIcon.getStyleClass().add("arcane-icon");
        FadeTransition glow = new FadeTransition(Duration.seconds(2.5), arcaneIcon);
        glow.setFromValue(1.0);
        glow.setToValue(0.4);
        glow.setCycleCount(Animation.INDEFINITE);
        glow.setAutoReverse(true);
        glow.play();

        box.getChildren().addAll(title, description, arcaneIcon);
        return box;
    }

    // more than five requests form
    private Node endedRequestsForm() {
        VBox box = new VBox(25);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("arcane-form");

        Label title = new Label("Your Arcane Membership is permanently denied.");
        title.getStyleClass().add("arcane-form-title");

        Label description = new Label("Your requests have been refused too many times. This decision is final and cannot be appealed.");
        description.setWrapText(true);
        description.setTextAlignment(TextAlignment.CENTER);
        description.setMaxWidth(500);
        description.getStyleClass().add("arcane-form-description");

        ImageView seal = new ImageView(new Image(getClass().getResource("/images/deinedArcaneIcon.png").toExternalForm()));
        seal.setFitWidth(150);
        seal.setFitHeight(150);
        seal.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.seconds(2.5), seal);
        fade.setFromValue(0.0);
        fade.setToValue(0.9);
        fade.play();

        box.getChildren().addAll(title, description, seal);
        return box;
    }

    private void handleRequestSubmission() {
        boolean success = arcaneRequestManager.makeArcaneRequest(currentCustomer);
        if (success) {
            showPopup("Request Sent",
                    "Your request has been received. Please await evaluation by one of our shop managers.",
                    "Understood");
            displayArcaneInfo();
        } else {
            showPopup("Request Denied",
                    "You can't send a request at this time. Contact support for more information.",
                    "Understood");
        }
    }

    private void showPopup(String title, String message, String buttonText) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/popup.fxml"));
            Parent root = loader.load();

            PopupController popupController = loader.getController();
            popupController.setPopupContent(title, message, buttonText);

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            popupStage.setScene(scene);
            popupStage.setResizable(false);
            popupStage.setTitle(title);
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // for FAQ questions buttons +/-
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

    @FXML
    private void toggleAnswer4() {
        boolean nowVisible = !answerBox4.isVisible();
        answerBox4.setVisible(nowVisible);
        answerBox4.setManaged(nowVisible);
        toggle4.setText(nowVisible ? "−" : "+");
    }

    // to view popup of list of requests
    @FXML
    private void viewArcaneRequests() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/order-details-popup.fxml"));
            Parent root = loader.load();

            OrderDetailsPopupController controller = loader.getController();
            Stage popupStage = new Stage();

            controller.setStage(popupStage);
            controller.setArcaneRequests(currentCustomer.getPersonID());

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(contentBox.getScene().getWindow());
            popupStage.setScene(scene);
            popupStage.setResizable(false);
            popupStage.setTitle("Arcane Membership Requests");
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRandomDescription() {
        List<String> arcaneMemberFlavour = List.of(
                "As an Arcane Member, you may now browse items that common customers will never see.",
                "The most curious objects often carry the highest price.",
                "All Arcane purchases are FINAL and any danger or harm caused to the customer is their own responsability.",
                "You now have access to the Arcane Catalogue, proceed with caution and shop responsibly.",
                "Some items in the Arcane Catalogue may alter perception. This is not considered a mistake.",
                "Every purchase from this moment is a pact. Please read well before purchasing.",
                "Your presence in the Arcane section has been logged. The shop is watching."
        );
        return arcaneMemberFlavour.get(new Random().nextInt(arcaneMemberFlavour.size()));
    }

}
