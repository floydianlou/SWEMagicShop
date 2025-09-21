package GUI.controller;

import BusinessLogic.ArcaneRequestManager;
import BusinessLogic.Utilities;
import DomainModel.ArcaneRequest;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.lang.reflect.Array;
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
    private ScrollPane pendingRequestsPane;
    @FXML
    private ScrollPane handledScrollPane;

    private ArcaneRequestManager arcaneManager = new ArcaneRequestManager();

    @FXML
    public void initialize() {
        arcaneItem.setImage(new Image(getClass().getResource("/images/arcaneItem.png").toExternalForm()));
        reversedArcaneItem.setImage(new Image(getClass().getResource("/images/arcaneItemReversed.png").toExternalForm()));
        pendingRequestsPane.setFitToWidth(true);
        handledScrollPane.setFitToWidth(true);
        loadPending();
        loadAll();
    }

    private void loadPending() {
        ArrayList<ArcaneRequest> pending = arcaneManager.viewPendingRequests();
        loadRequests(pending, pendingRequestsPane, "There are no current pending requests.");
    }

    private void loadRequests(ArrayList<ArcaneRequest> requests, ScrollPane currentPane, String message) {
        if (requests == null || requests.isEmpty()) {
            currentPane.setContent(makeEmptyBox(message));
            return;
        }

        VBox list = new VBox(12);
        list.setFillWidth(true);
        list.setPadding(new Insets(10));

        for (ArcaneRequest r : requests) {
            list.getChildren().add(createCard(r));
        }

        currentPane.setContent(list);
    }

    private void loadAll() {
        ArrayList<ArcaneRequest> requests = arcaneManager.viewAllRequests();
        loadRequests(requests, handledScrollPane, "No customer made any requests yet.");
    }

    private Node makeEmptyBox(String msg) {
        Label info = new Label(msg);
        info.getStyleClass().add("info-text");
        VBox wrapper = new VBox(info);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPrefHeight(400);
        return wrapper;
    }

    private Node createCard(ArcaneRequest r) {
        VBox card = new VBox(6);
        card.getStyleClass().add("cart-box");
        card.setPadding(new Insets(12));

        Label title = new Label("N." + r.getRequestID() + " - from " + r.getCustomerName());
        title.getStyleClass().add("request-title");

        Label status = new Label(r.getRequestStatus());
        status.getStyleClass().add("order-writing");

        String st = r.getRequestStatus().toUpperCase();
        if ("APPROVED".equals(st)) {
            status.setStyle("-fx-background-color: #9791c9; -fx-padding: 2 8; -fx-background-radius: 12;");
        } else if ("REJECTED".equals(st)) {
            status.setStyle("-fx-background-color: #caaae3; -fx-padding: 2 8; -fx-background-radius: 12;");
        } else if ("PENDING".equals(st)) {
            status.setStyle("-fx-background-color: #e3aae1; -fx-padding: 2 8; -fx-background-radius: 12;");
        }

        Label date = new Label("Last update: " + Utilities.formatOrderDate(r.getRequestDate()));
        date.getStyleClass().add("order-writing");

        Button viewBtn = new Button("View details");
        viewBtn.getStyleClass().add("choice-button");
        viewBtn.setOnAction(e -> showInfo(r)); // TODO popup

        HBox actions = new HBox(8, new Region(), viewBtn);
        HBox.setHgrow(actions.getChildren().get(0), Priority.ALWAYS);

        card.getChildren().addAll(title, status, date, actions);
        return card;
    }

    private String formatClient(ArcaneRequest r) {
        String name = r.getCustomerName();
        return (name != null && !name.isBlank())
                ? name + " (ID " + r.getCustomerID() + ")"
                : "Client #" + r.getCustomerID();
    }

    private void showInfo(ArcaneRequest r) {
        try {
            // 1) carica FXML del popup
            var url = getClass().getResource("/GUI/view/manager-arcane-popup.fxml");
            if (url == null) {
                showError("FXML non trovato: /GUI/view/manager-arcane-popup.fxml");
                return;
            }
            FXMLLoader fx = new FXMLLoader(url);
            Parent root = fx.load();

            // 2) controller del popup
            ManagerArcanePopup c = fx.getController();

            // 3) scena/stage trasparenti (niente bottoni di Windows)
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            // (opzionale) se vuoi forzare il CSS anche da Java:
            var css = getClass().getResource("/css/popup-styles.css");
            if (css != null && !scene.getStylesheets().contains(css.toExternalForm())) {
                scene.getStylesheets().add(css.toExternalForm());
            }

            Stage popup = new Stage(StageStyle.TRANSPARENT);
            popup.initModality(Modality.APPLICATION_MODAL);
            // owner = qualsiasi nodo già visibile, uso la contentBox
            if (contentBox != null && contentBox.getScene() != null) {
                popup.initOwner(contentBox.getScene().getWindow());
            }
            popup.setScene(scene);

            // 4) passaggio dati al popup
            c.setStage(popup);
            c.setRequest(r);

            // 5) callback per refresh UI dopo Approve/Reject
            c.setOnResult(success -> {
                // ricarica pannelli come già fai
                loadPending();
                loadAll();
            });

            // 6) trascinabilità prendendo la card del popup
            var card = root.lookup(".popup-card");
            final double[] d = new double[2];
            if (card != null) {
                card.setOnMousePressed(e -> {
                    d[0] = e.getScreenX() - popup.getX();
                    d[1] = e.getScreenY() - popup.getY();
                });
                card.setOnMouseDragged(e -> {
                    popup.setX(e.getScreenX() - d[0]);
                    popup.setY(e.getScreenY() - d[1]);
                });
            }

            // 7) mostra
            popup.show();

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Impossibile aprire il popup: " + ex.getMessage());
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}