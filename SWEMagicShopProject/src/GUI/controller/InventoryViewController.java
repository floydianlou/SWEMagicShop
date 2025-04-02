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
        inventoryManager = new InventoryManager(loggedCustomer);
        ArrayList<Item> inventory = inventoryManager.viewInventory(loggedCustomer.getPersonID());
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty");
        }

        for (Item item : inventory) {
            itemsContainer.getChildren().add(createItemCard(item));
        }

    }

    public HBox createItemCard(Item item) {
        HBox itemBox = new HBox(20);
        itemBox.setPadding(new Insets(15));
        itemBox.setPrefHeight(100);
        itemBox.getStyleClass().add("cart-item-box");

        //ImageView icon = new ImageView(new Image(getClass().getResource(item.getImagePath()).toExternalForm()));
        ImageView icon = new ImageView();
        String imagePath = item.getImagePath();
        if (imagePath != null) {
            icon.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
        } else {
            // Gestisci il caso in cui l'immagine non viene trovata
            icon.setImage(new Image(getClass().getResource("/images/products/default_armor.png").toExternalForm()));
        }
        icon.setFitWidth(60);
        icon.setFitHeight(80);

        Label itemName = new Label("Name: " + item.getItemName());
        itemName.getStyleClass().add("item-name");

        Label itemDescripion = new Label("Description: " + item.getItemDescription());
        itemDescripion.getStyleClass().add("order-writing");

        Label category = new Label(item.getItemCategory());
        category.getStyleClass().add("order-writing");

        String arcaneValue = item.isArcane() ? "Yes" : "No";
        Label arcane = new Label("Arcane: " + arcaneValue);
        arcane.getStyleClass().add("order-writing");

        int[] price = Utilities.normalizeCurrencyArray(item.getCopperValue());
        Label total = new Label(String.format("Total: %d GP, %d SP, %d CP", price[0], price[1], price[2]));
        total.getStyleClass().add("item-price");

        VBox textBox = new VBox(5, itemName, itemDescripion, category, arcane, total);
        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox leftBox = new HBox(20, icon, textBox);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftBox, Priority.ALWAYS);


        return itemBox;
    }
}
