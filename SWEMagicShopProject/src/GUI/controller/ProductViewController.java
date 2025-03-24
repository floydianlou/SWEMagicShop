package GUI.controller;

import BusinessLogic.CartManager;
import BusinessLogic.Utilities;
import DomainModel.Customer;
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

    @FXML private ImageView cartIcon;
    @FXML private ImageView accountIcon;
    @FXML private ImageView productImage;

    @FXML private Button addToCartButton;

    private CartManager cartManager;
    private Item selectedProduct;

    public ProductViewController() {
        cartManager = new CartManager((Customer) LoggedUserManager.getInstance().getLoggedUser());
    }


    @FXML
    public void initialize() {
        selectedProduct = ItemViewManager.getInstance().getProductSelected();
        loadItem();
        cartIcon.setImage(new Image(getClass().getResource("/images/cart.png").toExternalForm()));
        accountIcon.setImage(new Image(getClass().getResource("/images/account.png").toExternalForm()));
    }

    private void loadItem(){
        productName.setText(selectedProduct.getItemName());
        productDescription.setText(selectedProduct.getItemDescription());
        productCategory.setText("Category: " + selectedProduct.getItemCategory());
        productArcane.setText("Arcane: " + (selectedProduct.isArcane() ? "Yes" : "No"));
        int[] price = Utilities.normalizeCurrencyArray(selectedProduct.getCopperValue());
        productPrice.setText(String.format("Price: %d GP, %d SP, %d CP", price[0], price[1], price[2]));
        productImage.setImage(new Image(getClass().getResource(selectedProduct.getImagePath()).toExternalForm()));
        addToCartButton.setOnAction(event -> cartManager.addItemToCart(selectedProduct));
    }

    @FXML
    private void handleAccountButton() {
        SceneController.loadScene("account-view.fxml");
    }

    @FXML
    private void handleCartButton() {
        SceneController.loadScene("cart-view.fxml");
    }

    @FXML
    private void handleBack() {
        ItemViewManager.getInstance().clearProductSelected();
        SceneController.loadScene("customer-shop-view.fxml");
    }

}
