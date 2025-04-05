package GUI.controller;

import BusinessLogic.CartManager;
import BusinessLogic.Utilities;
import DomainModel.Item;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

public class ProductViewController {

    @FXML private Label productName;
    @FXML private Label productDescription;
    @FXML private Label productCategory;
    @FXML private Label productPrice;
    @FXML private ImageView productImage;

    @FXML private Button addToCartButton;
    @FXML private HBox itemBox;
    @FXML private HBox arcaneBox;

    private Item selectedProduct;
    private MainViewController mainViewController;

    public ProductViewController() {
    }


    @FXML
    public void initialize() {
        selectedProduct = ItemViewManager.getInstance().getProductSelected();
        ItemViewManager.getInstance().clearProductSelected();
        loadItem();
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        itemBox.setPrefWidth(screenWidth * 0.7);
        itemBox.setPrefHeight(screenHeight * 0.7);
        itemBox.getStyleClass().add("cart-box");
        itemBox.setAlignment(Pos.CENTER);
        itemBox.setSpacing(20);
    }

    private void loadItem(){
        productName.setText(selectedProduct.getItemName());
        productName.getStyleClass().add("item-name");

        productDescription.setText(selectedProduct.getItemDescription());
        productDescription.getStyleClass().add("order-writing");
        productDescription.wrapTextProperty().setValue(true);

        productCategory.setText(selectedProduct.getItemCategory());
        productCategory.getStyleClass().add("order-writing");

        if (selectedProduct.isArcane()) {
            ImageView arcaneIcon = new ImageView(new Image(getClass().getResource("/images/arcaneItem.png").toExternalForm()));
            arcaneIcon.setFitWidth(40);
            arcaneIcon.setFitHeight(40);

            arcaneBox.getChildren().addAll(arcaneIcon);
            arcaneBox.setAlignment(Pos.CENTER_LEFT);
        }

        int[] price = Utilities.normalizeCurrencyArray(selectedProduct.getCopperValue());
        productPrice.setText(String.format("%d GP, %d SP, %d CP", price[0], price[1], price[2]));
        productPrice.getStyleClass().add("item-price");

        productImage.setImage(new Image(getClass().getResource(selectedProduct.getImagePath()).toExternalForm()));
        productImage.setFitWidth(550);
        productImage.setFitHeight(450);

        // TODO temporary fix for mistaken quantity when added to cart
        addToCartButton.setOnAction(event -> {
            Item i = new Item(
                    selectedProduct.getItemID(),
                    selectedProduct.getItemName(),
                    selectedProduct.getItemDescription(),
                    selectedProduct.getItemCategory(),
                    1,
                    selectedProduct.isArcane(),
                    selectedProduct.getCopperValue(),
                    selectedProduct.getImagePath());
            CartManager.getInstance().addItemToCart(i);
            mainViewController.updateCartIcon();});
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }


}