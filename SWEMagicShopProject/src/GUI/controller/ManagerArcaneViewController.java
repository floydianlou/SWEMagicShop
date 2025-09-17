package GUI.controller;

import BusinessLogic.ArcaneRequestManager;
import BusinessLogic.Utilities;
import DomainModel.ArcaneRequest;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.stream.Collectors;

// TODO FINISH THIS DO NOT TOUCH IT'S INCOMPLETE FOR A LOT OF REASONS

public class ManagerArcaneViewController {

    @FXML
    private VBox contentBox;
    @FXML
    private ImageView arcaneItem;
    @FXML
    private ImageView reversedArcaneItem;
    @FXML
    private VBox pendingList;
    @FXML private ScrollPane handledScrollPane;

    private ArcaneRequestManager arcaneManager = new ArcaneRequestManager();

    @FXML
    public void initialize() {
        arcaneItem.setImage(new Image(getClass().getResource("/images/arcaneItem.png").toExternalForm()));
        reversedArcaneItem.setImage(new Image(getClass().getResource("/images/arcaneItemReversed.png").toExternalForm()));
        loadPending();
        loadAll();
    }

    private void loadPending() {
        ArrayList<ArcaneRequest> pending = arcaneManager.viewPendingRequests();
        pendingList.getChildren().clear();

        if (pending == null || pending.isEmpty()) {
            pendingList.getChildren().add(emptyBox("No pending requests"));
            return;
        }
        for (ArcaneRequest r : pending) {
            pendingList.getChildren().add(createPendingCard(r));
        }
    }

    private void loadAll() {
        ArrayList<ArcaneRequest> requests = arcaneManager.viewAllRequests(); // 👈 tutte, nessun filtro

        if (requests == null || requests.isEmpty()) {
            handledScrollPane.setContent(makeEmptyBox("No requests yet."));
            return;
        }

        VBox list = new VBox(12);
        list.setFillWidth(true);
        list.setPadding(new Insets(10));

        for (ArcaneRequest r : requests) {
            list.getChildren().add(createHandledCard(r));
        }

        handledScrollPane.setContent(list);
    }

    private Node makeEmptyBox(String msg) {
        Label info = new Label(msg);
        info.getStyleClass().add("info-text");
        VBox wrapper = new VBox(info);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPrefHeight(400);
        return wrapper;
    }

    private Node createPendingCard(ArcaneRequest r) {
        VBox card = new VBox(6);
        card.getStyleClass().add("cart-box");
        card.setPadding(new Insets(12));

        Label title = new Label("Request #" + r.getRequestID() + " — " + formatClient(r));
        title.getStyleClass().add("popup-title");

        Label status = new Label("Status: " + r.getRequestStatus());
        status.getStyleClass().add("order-writing");

        Label date = new Label("Submitted: " + Utilities.formatOrderDate(r.getRequestDate()));
        date.getStyleClass().add("order-writing");

        Button viewBtn = new Button("View");
        viewBtn.getStyleClass().add("choice-button");
        viewBtn.setOnAction(e -> showInfo(r)); // TODO create manage popup

        HBox actions = new HBox(8, new Region(), viewBtn);
        HBox.setHgrow(actions.getChildren().get(0), Priority.ALWAYS);

        card.getChildren().addAll(title, status, date, actions);
        return card;
    }

    private Node createHandledCard(ArcaneRequest r) {
        VBox card = new VBox(6);
        card.getStyleClass().add("cart-box");
        card.setPadding(new Insets(12));

        Label title = new Label("Request #" + r.getRequestID() + " — " + formatClient(r));
        title.getStyleClass().add("popup-title");


        Label status = new Label(r.getRequestStatus());
        status.getStyleClass().add("order-writing");


        String st = r.getRequestStatus().toUpperCase();
        if ("APPROVED".equals(st)) {
            status.setStyle("-fx-background-color: #b3e5c5; -fx-padding: 2 8; -fx-background-radius: 12;");
        } else if ("REJECTED".equals(st)) {
            status.setStyle("-fx-background-color: #f5c2c7; -fx-padding: 2 8; -fx-background-radius: 12;");
        }

        Label date = new Label("Decision date: " + r.getRequestDate());
        date.getStyleClass().add("order-writing");

        Button viewBtn = new Button("View");
        viewBtn.getStyleClass().add("choice-button");
        viewBtn.setOnAction(e -> showInfo(r));

        HBox actions = new HBox(8, new Region(), viewBtn);
        HBox.setHgrow(actions.getChildren().get(0), Priority.ALWAYS);

        card.getChildren().addAll(title, status, date, actions);
        return card;
    }

    private Node emptyBox(String message) {
        VBox box = new VBox(6);
        box.setPadding(new Insets(12));
        Label l = new Label(message);
        l.getStyleClass().add("order-writing");
        box.getChildren().add(l);
        return box;
    }

    private String formatClient(ArcaneRequest r) {
        String name = r.getCustomerName();
        return (name != null && !name.isBlank())
                ? name + " (ID " + r.getCustomerID() + ")"
                : "Client #" + r.getCustomerID();
    }

    private void showInfo(ArcaneRequest r) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText("Arcane Request #" + r.getRequestID());
        a.setContentText(
                "Client: " + formatClient(r) +
                        "\nStatus: " + r.getRequestStatus() +
                        "\nDate: " + r.getRequestDate()
        );
        a.showAndWait();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}