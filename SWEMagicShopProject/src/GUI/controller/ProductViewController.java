package GUI.controller;

import BusinessLogic.CartManager;
import BusinessLogic.StoreManager;
import BusinessLogic.Utilities;
import DomainModel.Customer;
import DomainModel.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class ProductViewController {

    private Customer loggedCustomer;

    @FXML private Label productName;
    @FXML private Label productDescription;
    @FXML private Label productCategory;
    @FXML private Label productArcane;
    @FXML private Label productPrice;
    @FXML private Label errorLabel;

    @FXML private ImageView cartIcon;
    @FXML private ImageView accountIcon;
    @FXML private ImageView productImage;

    @FXML private Button addToCartButton;


    private StoreManager storeManager;
    private CartManager cartManager;
    private Item product;

    public ProductViewController() {
        storeManager = new StoreManager();
        cartManager = new CartManager((Customer) LoggedUserManager.getInstance().getLoggedUser());
        this.product = ItemViewManager.getInstance().getProductSelected();
    }

    @FXML
    public void initialize() {
        loggedCustomer = (Customer) LoggedUserManager.getInstance().getLoggedUser();
        loadItem();
        cartIcon.setImage(new Image(getClass().getResource("/images/cart.png").toExternalForm()));
        accountIcon.setImage(new Image(getClass().getResource("/images/account.png").toExternalForm()));

    }

    private void loadItem(){
        productName.setText(product.getItemName());
        productDescription.setText(product.getItemDescription());
        productCategory.setText("Category: " + product.getItemCategory());
        productArcane.setText("Arcane: " + (product.isArcane() ? "Yes" : "No"));
        int[] price = Utilities.normalizeCurrencyArray(product.getCopperValue());
        productPrice.setText(String.format("Price: %d GP, %d SP, %d CP", price[0], price[1], price[2]));
        productImage.setImage(new Image(getClass().getResource(product.getImagePath()).toExternalForm()));
        addToCartButton.setOnAction(event -> cartManager.addItemToCart(product));
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
        SceneController.loadScene("choose-registration-type.fxml");
    }

}
