package GUI.controller;

import BusinessLogic.ArcaneRequestManager;
import BusinessLogic.CustomerOrderManager;
import BusinessLogic.Utilities;
import DomainModel.ArcaneRequest;
import DomainModel.Item;
import DomainModel.Order;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.io.File;

public class OrderDetailsPopupController {

    @FXML private Button backButton;
    @FXML private Label titleLabel;
    @FXML private Label statusLabel;
    @FXML private Label dateLabel;
    @FXML private Label priceLabel;
    @FXML private ScrollPane objectsScrollPane;

    private Stage stage;

    private CustomerOrderManager customerOrderManager;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOrder(Order order) {
        titleLabel.setText("Order #" + order.getOrderID() + " details:");
        statusLabel.setText("Status: " + order.getOrderStatus());
        dateLabel.setText("Submitted: " + Utilities.formatOrderDate(order.getOrderDate()));
        int[] price = Utilities.normalizeCurrencyArray(order.getTotalCP());
        priceLabel.setText(String.format("Total: %d GP, %d SP, %d CP", price[0], price[1], price[2]));
        backButton.setText("Back to orders");


        ArrayList<Item> itemList = customerOrderManager.viewOrderItems(order.getOrderID());


        VBox itemsBox = new VBox(10);
        itemsBox.setPadding(new Insets(10));

        // items rows
        for (Item item : itemList) {
            HBox itemRow = new HBox(20);
            itemRow.setPadding(new Insets(10));
            itemRow.setAlignment(Pos.CENTER_LEFT);
            itemRow.getStyleClass().add("cart-item-box");

            ImageView itemIcon = new ImageView();
            try{
                File imageFile = new File("SWEMagicShopProject/src" + item.getImagePath());
                if (!imageFile.exists()) {
                    System.out.println("Image file does NOT exist!");
                } else {
                    Image img = new Image(imageFile.toURI().toString());
                    itemIcon.setImage(img);
                }
            }
            catch (Exception e) {
                System.out.println("Error Image Not Loaded: " + e.getMessage());
            }
            itemIcon.setFitWidth(100);
            itemIcon.setFitHeight(100);
            itemIcon.getStyleClass().add("cart-image");

            // order info
            Label name = new Label(item.getItemName());
            name.getStyleClass().add("item-name");

            Label quantity = new Label(" x" + item.getItemQuantity());
            quantity.getStyleClass().add("item-price");

            HBox nameAndQuantity = new HBox(5, name, quantity);
            nameAndQuantity.setAlignment(Pos.CENTER_LEFT);

            Label category = new Label(item.getItemCategory());
            category.getStyleClass().add("order-writing");

            int totalCP = item.getCopperValue() * item.getItemQuantity();
            int[] itemPrice = Utilities.normalizeCurrencyArray(totalCP);
            Label itemPriceLabel = new Label(String.format("%d GP, %d SP, %d CP", itemPrice[0], itemPrice[1], itemPrice[2]));
            itemPriceLabel.getStyleClass().add("order-writing");

            HBox arcaneRow = null;
            if (item.isArcane()) {
                ImageView arcaneIcon = new ImageView(new Image(getClass().getResource("/images/arcaneItem.png").toExternalForm()));
                arcaneIcon.setFitWidth(18);
                arcaneIcon.setFitHeight(18);

                Label arcaneLabel = new Label("Arcane");
                arcaneLabel.getStyleClass().add("order-writing");

                arcaneRow = new HBox(5, arcaneIcon, arcaneLabel);
                arcaneRow.setAlignment(Pos.CENTER_LEFT);
            }

            VBox textBox = new VBox(5, nameAndQuantity, category, itemPriceLabel);
            if (arcaneRow != null) {
                textBox.getChildren().add(arcaneRow);
            }

            itemRow.getChildren().addAll(itemIcon, textBox);

            itemsBox.getChildren().add(itemRow);
        }

        objectsScrollPane.setContent(itemsBox);
    }

    public void setArcaneRequests(int customerID) {

        titleLabel.setText("Your Arcane Membership Requests:");

        ArcaneRequestManager manager = new ArcaneRequestManager();
        ArrayList<ArcaneRequest> requests = manager.viewRequestsByCustomer(customerID);

        if (requests == null || requests.isEmpty()) {
            statusLabel.setText("");
            dateLabel.setText("");
            priceLabel.setText("");
            Label info = new Label("You haven’t made any requests yet.");
            info.getStyleClass().add("info-text");

            VBox wrapper = new VBox(info);
            wrapper.setAlignment(Pos.CENTER);
            wrapper.setPrefHeight(400);

            objectsScrollPane.setContent(wrapper);
            backButton.setText("Back to status");
            return;
        }

        ArcaneRequest lastRequest = requests.get(0);

        statusLabel.setText("Your current request status: " + lastRequest.getRequestStatus());
        dateLabel.setText("Last request submitted: " + Utilities.formatOrderDate(lastRequest.getRequestDate()));
        priceLabel.setText("Total requests sent: " + requests.size());

        VBox requestsBox = new VBox(10);
        requestsBox.setPadding(new Insets(10));
        requestsBox.setFillWidth(true);

        for (ArcaneRequest request : requests) {
            VBox requestRow = new VBox(5);
            requestRow.setPadding(new Insets(10));
            requestRow.getStyleClass().add("cart-item-box");

            Label id = new Label("Request ID: " + request.getRequestID());
            Label status = new Label("Status: " + request.getRequestStatus());
            Label date = new Label("Date: " + Utilities.formatOrderDate(request.getRequestDate()));

            id.getStyleClass().add("item-name");
            status.getStyleClass().add("order-writing");
            date.getStyleClass().add("order-writing");

            requestRow.getChildren().addAll(id, status, date);
            requestsBox.getChildren().add(requestRow);
        }

        backButton.setText("Back to status");
        objectsScrollPane.setContent(requestsBox);
    }


    @FXML
    private void handleClosePopup() {
        if (stage != null) stage.close();
    }

    public void setCustomerOrderManager(CustomerOrderManager customerOrderManager) {
        this.customerOrderManager = customerOrderManager;
    }
}