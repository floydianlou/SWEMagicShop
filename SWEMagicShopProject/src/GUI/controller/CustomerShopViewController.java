package GUI.controller;

import BusinessLogic.CartManager;
import BusinessLogic.StoreManager;
import DomainModel.Customer;
import DomainModel.Item;
import BusinessLogic.Utilities;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
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

    @FXML private ImageView searchIcon;

    @FXML private GridPane gridPane;

    private ArrayList<String> allCategories;
    private ArrayList<Item> allProductsSearched;
    private ArrayList<Item> allProducts;

    private StoreManager storeManager;
    private MainViewController mainViewController;

    public CustomerShopViewController() {
        storeManager = new StoreManager();
    }

    @FXML
    public void initialize() {
        loggedCustomer = (Customer) LoggedUserManager.getInstance().getLoggedUser();
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
                filterCategory.toFront();
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
                filterArcane.toFront();
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
            if(loggedCustomer.isArcaneMember()){
                allProducts = storeManager.listProducts();
            }else{
                allProducts = storeManager.searchProductsByArcane("false");
            }
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
                    if(loggedCustomer.isArcaneMember()){
                        allProductsSearched = storeManager.searchProductsByCategory(filterCategory.getValue());
                    }else{
                        allProductsSearched = storeManager.searchProductsByCategoryNonArcane(filterCategory.getValue());
                    }
                    break;
                case "Name":
                    String name = searchBar.getText();
                    if(name.isEmpty()){
                        errorLabel.setText("Enter product name.");
                        return;
                    }
                    try{
                        if(loggedCustomer.isArcaneMember()){
                            allProductsSearched = storeManager.searchProductsByName(searchBar.getText().trim());
                        } else{
                            allProductsSearched = storeManager.searchProductsByNameNonArcane(searchBar.getText().trim());
                        }
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
                        if(loggedCustomer.isArcaneMember()){
                            allProductsSearched = storeManager.searchProductsByDescription(searchBar.getText().trim());
                        } else{
                            allProductsSearched = storeManager.searchProductsByDescriptionNonArcane(searchBar.getText().trim());
                        }
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
                        if(loggedCustomer.isArcaneMember()){
                            allProductsSearched = storeManager.searchProductsByPrice(minPriceValue, maxPriceValue);
                        } else{
                            allProductsSearched = storeManager.searchProductsByPriceNonArcane(minPriceValue, maxPriceValue);
                        }
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
                    if(loggedCustomer.isArcaneMember()){
                        allProductsSearched = storeManager.searchProductsByArcane(filterArcane.getValue().trim());
                    }else if(filterArcane.getValue().equals("true")){
                        errorLabel.setText("Arcane Items cannot be seen by a non-Arcane member.");
                        return;
                    }else{
                        allProductsSearched = storeManager.searchProductsByArcane("false");
                    }
                    break;
                case "All":
                    try{
                        if(loggedCustomer.isArcaneMember()) {
                            allProductsSearched = storeManager.listProducts();
                        } else {
                            allProductsSearched = storeManager.searchProductsByArcane("false");
                        }
                    } catch (RuntimeException e) {
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
            try{
                File imageFile = new File("SWEMagicShopProject/src" + product.getImagePath());
                if (!imageFile.exists()) {
                    System.out.println("Image file does NOT exist!");
                } else {
                    Image img = new Image(imageFile.toURI().toString());
                    productImage.setImage(img);
                }
            }
            catch (Exception e) {
                System.out.println("Error Image Not Loaded: " + e.getMessage());
            }
            productImage.setFitWidth(300);
            productImage.setFitHeight(300);

            Button productName = new Button(product.getItemName());
            productName.getStyleClass().add("product-name");
            productName.setOnMouseClicked(event -> {viewProductButton(product);});

            int[] price = Utilities.normalizeCurrencyArray(product.getCopperValue());
            Label productPrice = new Label(String.format("%d GP, %d SP, %d CP", price[0], price[1], price[2]));

            Button addToCartButton = new Button("Add to Cart");
            addToCartButton.getStyleClass().add("add-to-cart-button");
            addToCartButton.setOnMouseClicked(_ -> {
                //TODO: temporary fix for mistaken quantity when added to cart
                Item itemToAdd = new Item(
                        product.getItemID(),
                        product.getItemName(),
                        product.getItemDescription(),
                        product.getItemCategory(),
                        1,
                        product.isArcane(),
                        product.getCopperValue(),
                        product.getImagePath()
                );
                CartManager.getInstance().addItemToCart(itemToAdd);
                mainViewController.updateCartIcon();});

            HBox buttonContainer = new HBox(addToCartButton);
            buttonContainer.setAlignment(Pos.BOTTOM_RIGHT);

            productBox.getChildren().addAll(productImage, productName, productPrice, buttonContainer);

            gridPane.add(productBox, col, row);

            col++;
            if (col == maxCols) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    private void viewProductButton(Item selectedProduct) {
        ItemViewManager.getInstance().setProductSelected(selectedProduct);
        mainViewController.loadContent("product-view.fxml");
        mainViewController.updateTopBar("product");
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

}