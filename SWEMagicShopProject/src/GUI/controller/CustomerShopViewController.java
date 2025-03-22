package GUI.controller;

import BusinessLogic.StoreManager;
import DomainModel.Item;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CustomerShopViewController {

    @FXML private TextField searchBar;
    @FXML private TextField minprice;
    @FXML private TextField maxprice;
    @FXML private ComboBox<String> filterDropDown;
    @FXML private ComboBox<String> filterArcane;
    @FXML private ComboBox<String> filterCategory;
    @FXML private Label errorLabel;
    @FXML private ImageView cartIcon;
    @FXML private ImageView accountIcon;
    @FXML private ImageView productIcon;
    @FXML private ImageView searchIcon;
    @FXML private GridPane gridPane;
    private ArrayList<String> allCategories;
    private ArrayList<Item> allProductsSearched;
    private ArrayList<Item> allProducts;

    private StoreManager storeManager;

    public CustomerShopViewController() {
        storeManager = new StoreManager();
    }

    @FXML
    public void initialize() {
        loadProducts();
        loadCategoryInDropdown();
        loadFilterInDropdown();
        loadFilterArcane();
        filterDropDown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateSearchField(newValue));
        cartIcon.setImage(new Image(getClass().getResource("/images/cart.png").toExternalForm()));
        accountIcon.setImage(new Image(getClass().getResource("/images/account.png").toExternalForm()));
        searchIcon.setImage(new Image(getClass().getResource("/images/searchIcon.png").toExternalForm()));
    }


    public void loadFilterInDropdown() {
        filterDropDown.getItems().add("Category");
        filterDropDown.getItems().add("Name");
        filterDropDown.getItems().add("Description");
        filterDropDown.getItems().add("Price");
        filterDropDown.getItems().add("Arcane");
        filterDropDown.getItems().add("All");
    }

    public void loadCategoryInDropdown() {
        try {
            allCategories = storeManager.getAllCategories(); }
        catch (RuntimeException e) {
            errorLabel.setText(e.getMessage());
        }
        for (String category : allCategories) {
            filterCategory.getItems().add(category);
        }
    }

    public void loadFilterArcane() {
        filterArcane.getItems().add("true");
        filterArcane.getItems().add("false");
    }

    private void updateSearchField(String filterType) {
        // Nascondi o mostra i campi in base al filtro selezionato
        switch (filterType) {
            case "Category":
                searchBar.setPromptText("Select a category...");
                searchBar.setDisable(true);
                minprice.setVisible(false);
                maxprice.setVisible(false);
                filterCategory.setVisible(true);
                filterArcane.setVisible(false);
                break;
            case "Name":
                searchBar.setPromptText("Enter product name...");
                searchBar.setDisable(false);
                minprice.setVisible(false);
                maxprice.setVisible(false);
                filterCategory.setVisible(false);
                filterArcane.setVisible(false);
                break;
            case "Description":
                searchBar.setPromptText("Enter product description...");
                searchBar.setDisable(false);
                minprice.setVisible(false);
                maxprice.setVisible(false);
                filterCategory.setVisible(false);
                filterArcane.setVisible(false);
                break;
            case "Price":
                searchBar.setPromptText("Enter price range...");
                searchBar.setDisable(true);
                minprice.setVisible(true);
                maxprice.setVisible(true);
                filterCategory.setVisible(false);
                filterArcane.setVisible(false);
                break;
            case "Arcane":
                searchBar.setPromptText("Select Arcane (true/false)...");
                searchBar.setDisable(true);
                minprice.setVisible(false);
                maxprice.setVisible(false);
                filterCategory.setVisible(false);
                filterArcane.setVisible(true);
                break;
            case "All":
                searchBar.setPromptText("All products...");
                searchBar.setDisable(true);
                minprice.setVisible(false);
                maxprice.setVisible(false);
                filterCategory.setVisible(false);
                filterArcane.setVisible(false);
                break;
        }
        addProductsToGrid(allProductsSearched);
    }

    private void loadProducts() {
        try{
            allProducts = storeManager.listProducts();
        } catch (RuntimeException e) {
            errorLabel.setText(e.getMessage());
        }

        addProductsToGrid(allProducts);
    }

    @FXML
    private void handleSearch() {
        try {
            gridPane.getChildren().clear();
            if (filterDropDown.getValue() == null) {
                errorLabel.setText("Please select a filter.");
                return;
            }
            switch (filterDropDown.getValue()) {
                case "Category":
                    allProductsSearched = storeManager.searchProductsByCategory(filterCategory.getValue());
                    break;
                case "Name":
                    String name = searchBar.getText();
                    if(name.isEmpty()){
                        errorLabel.setText("Enter product name.");
                        return;
                    }
                    allProductsSearched = storeManager.searchProducsByName(searchBar.getText().trim());
                    break;
                case "Description":
                    String description = searchBar.getText();
                    if(description.isEmpty()){
                        errorLabel.setText("Enter product description.");
                        return;
                    }
                    allProductsSearched = storeManager.searchProductsByDescription(searchBar.getText().trim());
                    break;
                case "Price":
                    int minPriceValue = 0;
                    int maxPriceValue = 0;
                    try {
                        minPriceValue = Integer.parseInt(minprice.getText().trim());
                        maxPriceValue = Integer.parseInt(maxprice.getText().trim());
                    } catch (NumberFormatException e) {
                        errorLabel.setText("Select a valid price.");
                    }
                    allProductsSearched = storeManager.searchProductsByPrice(minPriceValue, maxPriceValue);
                    break;
                case "Arcane":
                    allProductsSearched = storeManager.searchProductsByArcane(filterArcane.getValue().trim());
                    break;
                case "All":
                    allProductsSearched = storeManager.listProducts();
                    break;
            }
        } catch (RuntimeException e) {
            errorLabel.setText(e.getMessage());
        }
        catch (Exception ex) {
            errorLabel.setText("An unexpected error happened. Please try again.");
            ex.printStackTrace();
        }

        addProductsToGrid(allProductsSearched);
        allProducts.clear();
    }

    private void addProductsToGrid(ArrayList<Item> products) {
        for (Item product : products) {
            VBox productBox = new VBox();

            // Crea ImageView per visualizzare l'immagine del prodotto
            //TODO

            Label productName = new Label(product.getItemName());
            productName.setStyle("-fx-font-weight: bold;");
            Label productDescription = new Label(product.getItemDescription());
            productDescription.setStyle("-fx-font-weight: bold;");
            Label productPrice = new Label(String.format("$%d", product.getCopperValue()));

            productBox.getChildren().addAll(productName, productDescription, productPrice);

            gridPane.add(productBox, 0, gridPane.getRowCount());
        }
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
    private void handleProductButton() {
        SceneController.loadScene("product-view.fxml");
    }

}
