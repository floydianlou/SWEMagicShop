package GUI.controller;

import BusinessLogic.CustomerOrderManager;
import BusinessLogic.StoreManager;
import BusinessLogic.Utilities;
import DomainModel.Item;
import DomainModel.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;

public class EditItemPopUpController {

    @FXML private TextField itemName;
    @FXML private TextField itemDescription;
    @FXML private ComboBox<String> itemCategory;
    @FXML private CheckBox itemArcane;
    @FXML private TextField itemGP;
    @FXML private TextField itemSP;
    @FXML private TextField itemCP;
    @FXML private Label imagePathLabel;

    @FXML private Button changeImageButton;

    @FXML private ImageView penIcon1;
    @FXML private ImageView penIcon2;
    @FXML private ImageView itemImage;

    private Stage stage;
    private StoreManager storeManager;
    private Item selectedItem;
    private ArrayList<String> allCategories;
    private String selectedImagePath;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML private void handleClosePopup() {
        if (stage != null) stage.close();
    }

    public void setStoreManager(StoreManager storeManager) {
        this.storeManager = storeManager;
    }

    public void loadCategoryInDropdown() {
        allCategories = storeManager.getAllCategories();
        for (String category : allCategories) {
            itemCategory.getItems().add(category);
        }
    }

    public void loadPopUp() {
        loadItem();
        loadCategoryInDropdown();
        penIcon1.setImage(new Image(getClass().getResource("/images/penIcon.png").toExternalForm()));
        penIcon2.setImage(new Image(getClass().getResource("/images/penIcon.png").toExternalForm()));
    }

    public void loadItem() {
        selectedItem = ItemViewManager.getInstance().getProductSelected();
        itemName.setText(selectedItem.getItemName());
        itemDescription.setText(selectedItem.getItemDescription());
        itemCategory.setValue(selectedItem.getItemCategory());
        itemArcane.setSelected(selectedItem.isArcane());
        selectedImagePath = selectedItem.getImagePath();
        imagePathLabel.setText(selectedImagePath);

        int[] price = Utilities.normalizeCurrencyArray(selectedItem.getCopperValue());
        itemGP.setText(String.valueOf(price[0]));
        itemSP.setText(String.valueOf(price[1]));
        itemCP.setText(String.valueOf(price[2]));

        File imageFile = new File("SWEMagicShopProject/src" + selectedItem.getImagePath());
        if (!imageFile.exists()) {
            System.out.println("Image file does not exist!");
        } else {
            Image img = new Image(imageFile.toURI().toString());
            itemImage.setImage(img);
        }
    }

    public void editItem() {
        String name = itemName.getText();
        String description = itemDescription.getText();
        String category = itemCategory.getValue();
        boolean isArcane = itemArcane.isSelected();

        if (name.isEmpty() || description.isEmpty() || itemGP.getText().isEmpty() || itemSP.getText().isEmpty() || itemCP.getText().isEmpty() || category == null) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        if (selectedImagePath == null || selectedImagePath.isEmpty()) {
            switch (category) {
                case "Potions":
                    selectedImagePath = "/images/products/default_potion.png";
                    break;
                case "Armor":
                    selectedImagePath = "/images/products/default_armor.png";
                    break;
                case "Weapons":
                    selectedImagePath = "/images/products/default_weapon.png";
                    break;
                default:
                    showAlert("Error", "Please select a valid category.");
                    return;
            }
        }

        int copperValue;
        try {
            copperValue = (Integer.parseInt(itemGP.getText()) * 100) + (Integer.parseInt(itemSP.getText()) * 10) + Integer.parseInt(itemCP.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numeric values for the price.");
            return;
        }

        try {
            Item updatedItem = new Item(selectedItem.getItemID(), name, description, category, isArcane, copperValue, selectedImagePath);
            storeManager.updateProduct(updatedItem);
            showAlert("Success", "Item edited successfully.");
            handleClosePopup();
        } catch (RuntimeException e) {
            showAlert("Error", e.getMessage());
        }
    }


    private void showAlert(String title, String s) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/popup.fxml"));
            Parent root = loader.load();

            PopupController popupController = loader.getController();
            popupController.setPopupContent(title, s, "OK");

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.setTitle("Item Created");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            popupStage.setScene(scene);
            popupStage.setResizable(false);
            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChangeImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File newselectedFile = fileChooser.showOpenDialog(null);
        if (newselectedFile != null) {
            try {

                File directory = new File("SWEMagicShopProject/src/images/products");
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File newcopiedFile = new File(directory, newselectedFile.getName());

                Files.copy(newselectedFile.toPath(), newcopiedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                if (selectedImagePath != null && !selectedImagePath.equals("/images/products/default_potion.png") && !selectedImagePath.equals("/images/products/default_armor.png") && !selectedImagePath.equals("/images/products/default_weapon.png")) {
                    File oldImageFile = new File("SWEMagicShopProject/src" + selectedImagePath);
                    if (oldImageFile.exists()) {
                        boolean deleted = oldImageFile.delete();
                        if (!deleted) {
                            System.out.println("Error: unable to delete the old image file.");
                        }
                    }
                }

                selectedImagePath = "/images/products/" + newselectedFile.getName();
                imagePathLabel.setText(selectedImagePath);
                itemImage.setImage(new Image(newcopiedFile.toURI().toString()));

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Something went wrong while selecting your image.");
            }
        }
    }
}
