package GUI.controller;

import BusinessLogic.CartManager;
import BusinessLogic.Utilities;
import DomainModel.Item;
import Exceptions.OrderExceptions;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CartController {

    @FXML
    private VBox cartContainer;
    private CartManager cartManager;

    @FXML
    public void initialize() {
        cartManager = CartManager.getInstance();
        displayCartItems();
    }

    public void displayCartItems() {
        cartContainer.getChildren().clear();
        for (Item item : cartManager.getCartItems()) {
                HBox itemBox = createItemBox(item);
                cartContainer.getChildren().add(itemBox);
            }
        }

        public HBox createItemBox(Item cartItem) {
            HBox itemBox = new HBox(20);
            itemBox.setPadding(new Insets(15));
            itemBox.setPrefHeight(100);
            itemBox.getStyleClass().add("cart-item-box");


            Label name = new Label(cartItem.getItemName());
            name.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            int[] price = Utilities.normalizeCurrencyArray(cartItem.getCopperValue());
            Label productPrice = new Label(String.format("%d GP, %d SP, %d CP", price[0], price[1], price[2]));
            // TODO css styles

            VBox textBox = new VBox(5, name, productPrice);
            TextField quantityField = createQuantityField(cartItem, itemBox);

            // decrease button definition
            Button decreaseButton = new Button("-");
            decreaseButton.setOnAction(_ -> {
                if (cartItem.getItemQuantity() > 1) {
                    cartManager.reduceItemQuantity(cartItem);
                    quantityField.setText(String.valueOf(cartItem.getItemQuantity()));
                } else {
                    try {
                        cartManager.removeItemFromCart(cartItem);
                    } catch (OrderExceptions.ItemNotInCartException e) {
                        throw new RuntimeException(e);
                    }
                    cartContainer.getChildren().remove(itemBox);
                } });

            // increase button definition
            Button increaseButton = new Button("+");
            increaseButton.setOnAction(e -> {
                cartManager.increaseItemQuantity(cartItem);
                quantityField.setText(String.valueOf(cartItem.getItemQuantity()));
            });

            // remove button definition
            Button removeButton= new Button("🗑");
            removeButton.setOnAction(_ -> {
                try {
                    cartManager.removeItemFromCart(cartItem);
                } catch (OrderExceptions.ItemNotInCartException e) {
                    throw new RuntimeException(e);
                }
                cartContainer.getChildren().remove(itemBox);
            });



            HBox quantityBox = new HBox(5, decreaseButton, quantityField, increaseButton, removeButton);
            quantityBox.setAlignment(Pos.CENTER);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            itemBox.getChildren().addAll(textBox, spacer, quantityBox);
            return itemBox;

        }

        public TextField createQuantityField(Item cartItem, HBox itemBox) {
            TextField quantityField = new TextField(String.valueOf(cartItem.getItemQuantity()));
            quantityField.setPrefWidth(40);
            quantityField.setPrefHeight(30);
            quantityField.setAlignment(Pos.CENTER);
            quantityField.setStyle("-fx-border-radius: 5; -fx-background-radius: 5; -fx-font-size: 14px;");

            quantityField.focusedProperty().addListener((_, _, newValue) -> {
                if (!newValue) {
                    String input = quantityField.getText();
                    if (input.matches("\\d+")) { // if not a number it resets to old value
                        int newQuantity = Integer.parseInt(input);
                        if (newQuantity == 0) {
                            try {
                                cartManager.removeItemFromCart(cartItem);
                            } catch (OrderExceptions.ItemNotInCartException e) {
                                throw new RuntimeException(e);
                            }
                            cartContainer.getChildren().remove(itemBox);
                        } else {
                        cartManager.setItemQuantity(cartItem, newQuantity);
                        quantityField.setText(String.valueOf(cartItem.getItemQuantity())); }
                    } else {
                        quantityField.setText(String.valueOf(cartItem.getItemQuantity()));
                    }
                }
            });
            return quantityField;
        }

    }