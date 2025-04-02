package GUI.controller;

import BusinessLogic.InventoryManager;
import BusinessLogic.Utilities;
import DomainModel.Customer;
import DomainModel.Item;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.ArrayList;


public class InventoryViewController {

    @FXML private VBox itemsContainer;
    @FXML private ImageView sunIcon;
    @FXML private ImageView reversedSunIcon;

    private InventoryManager inventoryManager;
    private Customer loggedCustomer;

    @FXML
    public void initialize() {
        loggedCustomer = (Customer) LoggedUserManager.getInstance().getLoggedUser();
        sunIcon.setImage(new Image(getClass().getResource("/images/sunIcon.png").toExternalForm()));
        reversedSunIcon.setImage(new Image(getClass().getResource("/images/sunIconFlipped.png").toExternalForm()));
        inventoryManager = new InventoryManager();
        loadInventory();
    }

    private void loadInventory() {
        ArrayList<Item> inventory = inventoryManager.viewInventory(loggedCustomer);
        for (Item item : inventory) {
            itemsContainer.getChildren().add(createItemCard(item));
        }
    }

    public HBox createItemCard(Item item) {
        HBox itemBox = new HBox(20);
        itemBox.setPadding(new Insets(15));
        itemBox.setPrefHeight(100);
        itemBox.getStyleClass().add("cart-item-box");

        ImageView icon = new ImageView(new Image(getClass().getResource(item.getImagePath()).toExternalForm()));
        icon.setFitWidth(75);
        icon.setFitHeight(100);

        Label itemName = new Label(item.getItemName());
        itemName.getStyleClass().add("item-name");

        Label itemDescripion = new Label("Description: " + item.getItemDescription());
        itemDescripion.getStyleClass().add("order-writing");
        itemDescripion.wrapTextProperty().setValue(true);

        Label category = new Label(item.getItemCategory());
        category.getStyleClass().add("order-writing");

        HBox arcaneRow = null;
        if (item.isArcane()) {
            ImageView arcaneIcon = new ImageView(new Image(getClass().getResource("/images/arcaneItem.png").toExternalForm()));
            arcaneIcon.setFitWidth(18);
            arcaneIcon.setFitHeight(18);

            arcaneRow = new HBox(5, arcaneIcon);
            arcaneRow.setAlignment(Pos.CENTER_LEFT);
        }

        HBox arcaneCategoryBox = new HBox(5, category);
        if(arcaneRow != null) {
            arcaneCategoryBox.getChildren().add(arcaneRow);
        }

        int[] price = Utilities.normalizeCurrencyArray(item.getCopperValue());
        Label total = new Label(String.format("%d GP, %d SP, %d CP", price[0], price[1], price[2]));
        total.getStyleClass().add("item-price");

        VBox textBox = new VBox(5, itemName, itemDescripion, arcaneCategoryBox, total);
        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox leftBox = new HBox(20, icon, textBox);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftBox, Priority.ALWAYS);

        Label quantityText = new Label("Quantity:");
        quantityText.getStyleClass().add("order-writing");
        Label quantity = new Label(String.valueOf(item.getItemQuantity()));
        quantity.getStyleClass().add("order-writing");

        VBox quantityBox = new VBox(5, quantityText, quantity);
        quantityBox.setAlignment(Pos.CENTER);

        HBox rightBox = new HBox(20, quantityBox);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        itemBox.getChildren().addAll(leftBox, rightBox);

        return itemBox;
    }
}
