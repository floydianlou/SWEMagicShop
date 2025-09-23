package GUI.controller;

import BusinessLogic.CartManager;
import BusinessLogic.CustomerOrderManager;
import BusinessLogic.Utilities;
import BusinessLogic.WalletManager;
import DomainModel.Customer;
import DomainModel.Item;
import Exceptions.OrderExceptions;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CartController {

    @FXML private VBox cartContainer;
    @FXML private Label totalLabel;
    @FXML private Button payButton;
    @FXML private Label errorLabel;
    private CartManager cartManager;
    CustomerOrderManager customerOrderManager;
    private MainViewController mainViewController;
    @FXML private ImageView leftStar;
    @FXML private ImageView rightStar;

    @FXML
    public void initialize() {
        leftStar.setImage(new Image(getClass().getResource("/images/shoppingCartStars.png").toExternalForm()));
        rightStar.setImage(new Image(getClass().getResource("/images/shoppingCartStarsFlipped.png").toExternalForm()));
        cartManager = CartManager.getInstance();
        displayCartItems();
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void displayCartItems() {
        cartContainer.getChildren().clear();
        for (Item item : cartManager.getCartItems()) {
                HBox itemBox = createItemBox(item);
                cartContainer.getChildren().add(itemBox);
            }
        updateTotal();
        }

        public HBox createItemBox(Item cartItem) {
            HBox itemBox = new HBox(20);
            itemBox.setPadding(new Insets(10));
            itemBox.setPrefHeight(100);
            itemBox.getStyleClass().add("cart-item-box");

            // image view section
            ImageView itemIcon = new ImageView(new Image(getClass().getResource(cartItem.getImagePath()).toExternalForm()));
            itemIcon.setFitWidth(80);
            itemIcon.setFitHeight(100);
            itemIcon.getStyleClass().add("cart-image");

            StackPane imageContainer = new StackPane(itemIcon);

            Label name = new Label(cartItem.getItemName());
            name.getStyleClass().add("item-name");

            int[] price = Utilities.normalizeCurrencyArray(cartItem.getCopperValue());
            Label productPrice = new Label(String.format("%d GP, %d SP, %d CP", price[0], price[1], price[2]));
            productPrice.getStyleClass().add("item-price");

            VBox textBox = new VBox(5, name, productPrice);
            TextField quantityField = createQuantityField(cartItem, itemBox);

            // decrease button definition
            Button decreaseButton = new Button("-");
            decreaseButton.getStyleClass().add("quantity-button");
            decreaseButton.setOnAction(_ -> {
                if (cartItem.getItemQuantity() > 1) {
                    cartManager.reduceItemQuantity(cartItem);
                    quantityField.setText(String.valueOf(cartItem.getItemQuantity()));
                    updateTotal();
                } else {
                    try {
                        cartManager.removeItemFromCart(cartItem);
                        updateTotal();
                    } catch (OrderExceptions.ItemNotInCartException e) {
                        throw new RuntimeException(e);
                    }
                    cartContainer.getChildren().remove(itemBox);
                } });

            // increase button definition
            Button increaseButton = new Button("+");
            increaseButton.getStyleClass().add("quantity-button");
            increaseButton.setOnAction(e -> {
                cartManager.increaseItemQuantity(cartItem);
                quantityField.setText(String.valueOf(cartItem.getItemQuantity()));
                updateTotal();
            });

            // remove button definition
            Button removeButton= new Button("🗑");
            removeButton.getStyleClass().add("remove-button");
            removeButton.setOnAction(_ -> {
                try {
                    cartManager.removeItemFromCart(cartItem);
                    updateTotal();
                } catch (OrderExceptions.ItemNotInCartException e) {
                    throw new RuntimeException(e);
                }
                cartContainer.getChildren().remove(itemBox);
                updateTotal();
            });



            HBox quantityBox = new HBox(5, decreaseButton, quantityField, increaseButton, removeButton);
            quantityBox.setAlignment(Pos.CENTER);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            itemBox.getChildren().addAll(imageContainer, textBox, spacer, quantityBox);
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
                                updateTotal();
                            } catch (OrderExceptions.ItemNotInCartException e) {
                                throw new RuntimeException(e);
                            }
                            cartContainer.getChildren().remove(itemBox);
                        } else {
                        cartManager.setItemQuantity(cartItem, newQuantity);
                        quantityField.setText(String.valueOf(cartItem.getItemQuantity()));
                        updateTotal();}
                    } else {
                        quantityField.setText(String.valueOf(cartItem.getItemQuantity()));
                    }
                }
            });
            return quantityField;
        }

    public void updateTotal() {
        int totalCopper = 0;
        for (Item item : cartManager.getCartItems()) {
            totalCopper += item.getCopperValue() * item.getItemQuantity();
        }

        int[] price = Utilities.normalizeCurrencyArray(totalCopper);
        totalLabel.setText(String.format("%d GP, %d SP, %d CP", price[0], price[1], price[2]));
    }

    @FXML
    public void handlePlaceOrder() {
        try {
            customerOrderManager = new CustomerOrderManager();
            WalletManager currentCustomerWallet = new WalletManager();
            int orderID = customerOrderManager.createOrder((Customer) LoggedUserManager.getInstance().getLoggedUser(), CartManager.getInstance(), currentCustomerWallet);

            popupOrderComplete(orderID);

        } catch (OrderExceptions.EmptyCartException e) {
           errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        } catch (OrderExceptions.MissingFundsException ex) {
            errorLabel.setText(ex.getMessage());
            errorLabel.setVisible(true);
        } catch (OrderExceptions.OrderSaveException exception) {
            errorLabel.setText(exception.getMessage());
            errorLabel.setVisible(true);
        }
    }

    private void popupOrderComplete(int orderID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/popup.fxml"));
            Parent root = loader.load();

            PopupController popupController = loader.getController();
            popupController.setPopupContent("Order Successful!", "Your order (ID: " + orderID + ") was submitted successfully!", "Back to shopping");

            popupController.setMainViewController(mainViewController);

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            popupStage.setScene(scene);
            popupStage.setResizable(false);
            popupStage.setTitle("Order Successful!");
            popupStage.initOwner(cartContainer.getScene().getWindow());
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }