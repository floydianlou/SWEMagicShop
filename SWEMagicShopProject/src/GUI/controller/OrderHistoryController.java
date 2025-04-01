package GUI.controller;

import BusinessLogic.CustomerOrderManager;
import BusinessLogic.Utilities;
import DomainModel.Customer;
import DomainModel.Item;
import DomainModel.Order;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
        orderBox.getStyleClass().add("cart-item-box"); // stesso stile del carrello!

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

        orderBox.getChildren().addAll(icon, textBox);
        return orderBox;
    }
}