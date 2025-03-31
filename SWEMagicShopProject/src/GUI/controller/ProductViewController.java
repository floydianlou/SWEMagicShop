package GUI.controller;

import BusinessLogic.CartManager;
import BusinessLogic.Utilities;
import DomainModel.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductViewController {

    @FXML private Label productName;
    @FXML private Label productDescription;
    @FXML private Label productCategory;
    @FXML private Label productArcane;
    @FXML private Label productPrice;
    @FXML private ImageView productImage;

    @FXML private Button addToCartButton;

    private Item selectedProduct;
    private MainViewController mainViewController;

    public ProductViewController() {
    }


    @FXML
    public void initialize() {
        selectedProduct = ItemViewManager.getInstance().getProductSelected();
        ItemViewManager.getInstance().clearProductSelected();
        loadItem();
    }

    private void loadItem(){
        productName.setText(selectedProduct.getItemName());
        productDescription.setText(selectedProduct.getItemDescription());
        productCategory.setText("Category: " + selectedProduct.getItemCategory());
        productArcane.setText("Arcane: " + (selectedProduct.isArcane() ? "Yes" : "No"));
        int[] price = Utilities.normalizeCurrencyArray(selectedProduct.getCopperValue());
        productPrice.setText(String.format("Price: %d GP, %d SP, %d CP", price[0], price[1], price[2]));
        productImage.setImage(new Image(getClass().getResource(selectedProduct.getImagePath()).toExternalForm()));
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