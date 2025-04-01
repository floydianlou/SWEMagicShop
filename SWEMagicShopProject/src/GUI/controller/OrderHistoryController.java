package GUI.controller;

import BusinessLogic.CustomerOrderManager;
import BusinessLogic.Utilities;
import DomainModel.Customer;
import DomainModel.Item;
import DomainModel.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;

import static BusinessLogic.Utilities.createRoundedImageBox;

public class OrderHistoryController {

    private CustomerOrderManager customerOrderManager;

    @FXML
    private VBox ordersContainer;
    @FXML private ImageView moonIcon;
    @FXML private ImageView reversedMoonIcon;

    @FXML
    public void initialize() {
        moonIcon.setImage(new Image(getClass().getResource("/images/orderHistoryIcon.png").toExternalForm()));
        reversedMoonIcon.setImage(new Image(getClass().getResource("/images/orderHistoryIconFlipped.png").toExternalForm()));
        customerOrderManager = new CustomerOrderManager();
        ArrayList<Order> orderList = customerOrderManager.viewCustomerOrders((Customer) LoggedUserManager.getInstance().getLoggedUser());

        for (Order order : orderList) {
            ordersContainer.getChildren().add(createOrderCard(order));
        }

    }

    public HBox createOrderCard(Order order) {
        HBox orderBox = new HBox(20);
        orderBox.setPadding(new Insets(15));
        orderBox.setPrefHeight(100);
        orderBox.getStyleClass().add("cart-item-box");

        ImageView icon = new ImageView(new Image(getClass().getResource("/images/orderIcon.png").toExternalForm()));
        icon.setFitWidth(60);
        icon.setFitHeight(80);

        Label title = new Label("Order #" + order.getOrderID());
        title.getStyleClass().add("item-name");

        Label status = new Label(order.getOrderStatus());
        status.getStyleClass().add("order-writing");

        String orderDate = Utilities.formatOrderDate(order.getOrderDate());
        Label date = new Label("Submitted: " + orderDate);
        date.getStyleClass().add("order-writing");

        int[] price = Utilities.normalizeCurrencyArray(order.getTotalCP());
        Label total = new Label(String.format("Total: %d GP, %d SP, %d CP", price[0], price[1], price[2]));
        total.getStyleClass().add("item-price");

        VBox textBox = new VBox(5, title, status, date, total);
        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox leftBox = new HBox(20, icon, textBox);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftBox, Priority.ALWAYS);

        Button detailsButton = new Button("Order details");
        detailsButton.getStyleClass().add("choice-button");
        detailsButton.setOnAction(e -> popupOrderDetails(order));

        VBox buttonBox = new VBox(detailsButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);


        orderBox.getChildren().addAll(leftBox, buttonBox);
        return orderBox;
    }

    private void popupOrderDetails(Order order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/order-details-popup.fxml"));
            Parent root = loader.load();

            OrderDetailsPopupController controller = loader.getController();
            Stage popupStage = new Stage();

            controller.setStage(popupStage);
            controller.setCustomerOrderManager(customerOrderManager);
            controller.setOrder(order);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(ordersContainer.getScene().getWindow());
            popupStage.setScene(scene);
            popupStage.setResizable(false);
            popupStage.setTitle("Order Details");
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}