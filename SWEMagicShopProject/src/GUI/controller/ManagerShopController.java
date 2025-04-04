// File: GUI/controller/ManagerShopController.java
package GUI.controller;

import BusinessLogic.StoreManager;
import DomainModel.Item;
import DomainModel.Manager;
import BusinessLogic.Utilities;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.util.ArrayList;

public class ManagerShopController {

    @FXML private TextField searchBar;
    @FXML private TextField minpriceGP;
    @FXML private TextField maxpriceGP;
    @FXML private ComboBox<String> filterDropDown;
    @FXML private ComboBox<String> filterArcane;
    @FXML private ComboBox<String> filterCategory;
    @FXML private Label errorLabel;
    @FXML private ImageView searchIcon;
    @FXML private GridPane gridPane;

    private ArrayList<String> allCategories;
    private ArrayList<Item> allProductsSearched;
    private ArrayList<Item> allProducts;

    private StoreManager storeManager;
    private ManagerMainViewController mainViewController;

    public ManagerShopController() {
        storeManager = new StoreManager();
    }

    @FXML
    public void initialize() {
        // Fix: cast the logged user to Manager instead of Customer
        Manager loggedManager = (Manager) LoggedUserManager.getInstance().getLoggedUser();
        // LoggedManager can be used if needed in additional logic.
        loadProducts();
        loadCategoryInDropdown();
        loadFilterInDropdown();
        loadFilterArcane();
        filterDropDown.valueProperty().addListener((observable, oldValue, newValue) -> updateSearchField(newValue));
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
            allCategories = storeManager.getAllCategories();
        } catch (RuntimeException e) {
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
        try {
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
                    if (name.isEmpty()) {
                        errorLabel.setText("Enter product name.");
                        return;
                    }
                    allProductsSearched = storeManager.searchProducsByName(name.trim());
                    break;
                case "Description":
                    String description = searchBar.getText();
                    if (description.isEmpty()) {
                        errorLabel.setText("Enter product description.");
                        return;
                    }
                    allProductsSearched = storeManager.searchProductsByDescription(description.trim());
                    break;
                case "Price":
                    int minPrice;
                    int maxPrice;
                    try {
                        minPrice = Integer.parseInt(minpriceGP.getText().trim()) * 100;
                        maxPrice = Integer.parseInt(maxpriceGP.getText().trim()) * 100;
                    } catch (NumberFormatException e) {
                        errorLabel.setText("Select a valid price.");
                        return;
                    }
                    allProductsSearched = storeManager.searchProductsByPrice(minPrice, maxPrice);
                    break;
                case "Arcane":
                    if (filterArcane.getValue() == null) {
                        errorLabel.setText("Please select an arcane value.");
                        return;
                    }
                    allProductsSearched = storeManager.searchProductsByArcane(filterArcane.getValue().trim());
                    break;
                case "All":
                    allProductsSearched = storeManager.listProducts();
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
        gridPane.getChildren().clear();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        int row = 0;
        int col = 0;
        int maxCols = 4;
        for (Item product : products) {
            VBox productBox = new VBox();
            productBox.setSpacing(5);
            ImageView productImage = new ImageView();
            productImage.setImage(new Image(getClass().getResource(product.getImagePath()).toExternalForm()));
            productImage.setFitWidth(300);
            productImage.setFitHeight(300);
            Button productName = new Button(product.getItemName());
            productName.getStyleClass().add("product-name");
            productName.setOnMouseClicked(event -> editProductButton(product));
            int[] price = Utilities.normalizeCurrencyArray(product.getCopperValue());
            Label productPrice = new Label(String.format("%d GP, %d SP, %d CP", price[0], price[1], price[2]));
            productBox.getChildren().addAll(productImage, productName, productPrice);
            gridPane.add(productBox, col, row);
            col++;
            if (col == maxCols) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    private void editProductButton(Item selectedProduct) {
        ItemViewManager.getInstance().setProductSelected(selectedProduct);
        mainViewController.loadContent("item-edit-view.fxml");
        mainViewController.updateTopBar("product");
    }

    public void setMainViewController(ManagerMainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}