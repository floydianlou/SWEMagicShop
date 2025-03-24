package GUI.controller;

import BusinessLogic.StoreManager;
import DomainModel.Customer;
import DomainModel.Item;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CustomerShopViewController {

    private Customer loggedCustomer;

    @FXML private TextField searchBar;
    @FXML private TextField minpriceGP;
    @FXML private TextField maxpriceGP;


    @FXML private ComboBox<String> filterDropDown;
    @FXML private ComboBox<String> filterArcane;
    @FXML private ComboBox<String> filterCategory;

    @FXML private Label errorLabel;

    @FXML private ImageView cartIcon;
    @FXML private ImageView accountIcon;
    @FXML private ImageView productIcon;
    @FXML private ImageView searchIcon;

    @FXML private GridPane gridPane;
    @FXML private VBox filterContainer;
    @FXML private HBox priceFilterBox;

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
        filterDropDown.valueProperty().addListener((observable, oldValue, newValue) -> updateSearchField(newValue));
        cartIcon.setImage(new Image(getClass().getResource("/images/cart.png").toExternalForm()));
        accountIcon.setImage(new Image(getClass().getResource("/images/account.png").toExternalForm()));
        searchIcon.setImage(new Image(getClass().getResource("/images/searchIcon.png").toExternalForm()));
    }

    public void setLoggedCustomer(Customer loggedCustomer) {
        this.loggedCustomer = loggedCustomer;
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
        switch (filterType) {
            case "Category":
                searchBar.setPromptText("Select a category...");
                searchBar.setDisable(true);
                minpriceGP.setVisible(false);
                maxpriceGP.setVisible(false);
                filterCategory.setVisible(true);
                filterArcane.setVisible(false);
                break;
            case "Name":
                searchBar.setPromptText("Enter product name...");
                searchBar.setDisable(false);
                minpriceGP.setVisible(false);
                maxpriceGP.setVisible(false);
                filterCategory.setVisible(false);
                filterArcane.setVisible(false);
                break;
            case "Description":
                searchBar.setPromptText("Enter product description...");
                searchBar.setDisable(false);
                minpriceGP.setVisible(false);
                maxpriceGP.setVisible(false);
                filterCategory.setVisible(false);
                filterArcane.setVisible(false);
                break;
            case "Price":
                searchBar.setPromptText("Enter price range...");
                searchBar.setDisable(true);
                minpriceGP.setVisible(true);
                maxpriceGP.setVisible(true);
                filterCategory.setVisible(false);
                filterArcane.setVisible(false);
                break;
            case "Arcane":
                searchBar.setPromptText("Select Arcane (true/false)...");
                searchBar.setDisable(true);
                minpriceGP.setVisible(false);
                maxpriceGP.setVisible(false);
                filterCategory.setVisible(false);
                filterArcane.setVisible(true);
                break;
            case "All":
                searchBar.setPromptText("All products...");
                searchBar.setDisable(true);
                minpriceGP.setVisible(false);
                maxpriceGP.setVisible(false);
                filterCategory.setVisible(false);
                filterArcane.setVisible(false);
                break;
        }

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
            allProductsSearched = new ArrayList<>();
            switch (filterDropDown.getValue()) {
                case "Category":
                    if (filterCategory.getValue() == null) {
                        errorLabel.setText("Please select a category.");
                        return;
                    }
                    allProductsSearched = storeManager.searchProductsByCategory(filterCategory.getValue());
                    break;
                case "Name":
                    String name = searchBar.getText();
                    if(name.isEmpty()){
                        errorLabel.setText("Enter product name.");
                        return;
                    }
                    try{
                        allProductsSearched = storeManager.searchProducsByName(searchBar.getText().trim());
                    }catch (RuntimeException e) {
                        errorLabel.setText(e.getMessage());
                    }
                    break;
                case "Description":
                    String description = searchBar.getText();
                    if(description.isEmpty()){
                        errorLabel.setText("Enter product description.");
                        return;
                    }
                    try{
                        allProductsSearched = storeManager.searchProductsByDescription(searchBar.getText().trim());
                    }catch (RuntimeException e) {
                        errorLabel.setText(e.getMessage());
                    }
                    break;
                case "Price":
                    int minPriceValue = 0;
                    int maxPriceValue = 0;
                    int minPriceGP = 0;
                    int maxPriceGP = 0;

                    try {
                        minPriceGP = Integer.parseInt(minpriceGP.getText().trim());
                        maxPriceGP = Integer.parseInt(maxpriceGP.getText().trim());

                        minPriceValue = minPriceGP*100;
                        maxPriceValue = maxPriceGP*100;

                    } catch (NumberFormatException e) {
                        errorLabel.setText("Select a valid price.");
                        return;
                    }
                    try{
                        allProductsSearched = storeManager.searchProductsByPrice(minPriceValue, maxPriceValue);
                    }catch (IllegalArgumentException e){
                        errorLabel.setText(e.getMessage());
                        return;
                    }
                    catch (RuntimeException e) {
                        errorLabel.setText(e.getMessage());
                    }
                    break;
                case "Arcane":
                    if(filterArcane.getValue() == null){
                        errorLabel.setText("Please select an arcane.");
                        return;
                    }
                    allProductsSearched = storeManager.searchProductsByArcane(filterArcane.getValue().trim());
                    break;
                case "All":
                    try{
                        allProductsSearched = storeManager.listProducts();
                    }catch (RuntimeException e) {
                        errorLabel.setText(e.getMessage());
                    }
                    break;
            }
        } catch (Exception ex) {
            errorLabel.setText("An unexpected error happened. Please try again.");
            ex.printStackTrace();
        }

        if (allProductsSearched.isEmpty()) {
            errorLabel.setText("No products found.");
        } else {
            errorLabel.setText("");
        }
        addProductsToGrid(allProductsSearched);
    }

    private void addProductsToGrid(ArrayList<Item> products) {
        // 🔥 Pulisce la griglia prima di aggiungere nuovi prodotti
        gridPane.getChildren().clear();

        int row = 0; // Gestione righe
        int col = 0; // Gestione colonne
        int maxCols = 3; // Numero massimo di colonne per riga

        for (Item product : products) {
            VBox productBox = new VBox();

            // Crea le Label per il prodotto
            Label productName = new Label(product.getItemName());
            productName.setStyle("-fx-font-weight: bold;");
            Label productDescription = new Label(product.getItemDescription());
            productDescription.setStyle("-fx-font-weight: bold;");
            Label productPrice = new Label(String.format("$%d", product.getCopperValue()));

            // Aggiunge gli elementi alla VBox
            productBox.getChildren().addAll(productName, productDescription, productPrice);

            // Aggiunge il prodotto alla griglia
            gridPane.add(productBox, col, row);

            // Gestisce la posizione per creare una griglia
            col++;
            if (col == maxCols) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    private void handleAccountButton() {
        SceneController.loadSceneWithCustomer("account-view.fxml", loggedCustomer);
    }

    @FXML
    private void handleCartButton() {
        SceneController.loadSceneWithCustomer("cart-view.fxml", loggedCustomer);
    }

    @FXML
    private void handleProductButton() {
        SceneController.loadScene("product-view.fxml");
    }

}
