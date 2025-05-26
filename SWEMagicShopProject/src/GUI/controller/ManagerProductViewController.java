package GUI.controller;

import BusinessLogic.CartManager;
import BusinessLogic.Utilities;
import DomainModel.Item;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class ManagerProductViewController {

    @FXML private Label productName;
    @FXML private Label productDescription;
    @FXML private Label productCategory;
    @FXML private Label productPrice;
    @FXML private ImageView productImage;

    @FXML private Button editProductButton;
    @FXML private HBox itemBox;
    @FXML private HBox arcaneBox;

    private Item selectedProduct;
    private MainViewController mainViewController;


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

        try{
            File imageFile = new File("SWEMagicShopProject/src" + selectedProduct.getImagePath());
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
        //productImage.setImage(new Image(getClass().getResource(selectedProduct.getImagePath()).toExternalForm()));
        productImage.setFitWidth(550);
        productImage.setFitHeight(450);

        editProductButton.setOnAction(event -> {
            handleEditProduct();
        });
    }

    @FXML
    private void handleEditProduct() {
        /*try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/item-edit-popup.fxml"));
            Parent root = loader.load();

            AddItemPopupController controller = loader.getController();
            Stage popupStage = new Stage();

            controller.setStoreManager(storeManager);
            controller.setStage(popupStage);
            controller.loadPopUp();

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(addProductButton.getScene().getWindow());
            popupStage.setScene(scene);
            popupStage.setResizable(false);
            popupStage.setTitle("Add New Product To The Shop");
            popupStage.showAndWait();

            gridPane.getChildren().clear();
            loadProducts();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}
