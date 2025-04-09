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


public class AddItemPopupController {

    @FXML private TextField itemName;
    @FXML private TextField itemDescription;
    @FXML private ComboBox<String> itemCategory;
    @FXML private CheckBox itemArcane;
    @FXML private TextField itemGP;
    @FXML private TextField itemSP;
    @FXML private TextField itemCP;
    @FXML private Label imagePathLabel;

    @FXML private ImageView penIcon1;
    @FXML private ImageView penIcon2;
    @FXML private ImageView itemImage;

    private Stage stage;
    private StoreManager storeManager;
    private ArrayList<String> allCategories;
    private String selectedImagePath = "/images/products/default_potion.png"; // Default image


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
        loadCategoryInDropdown();
        penIcon1.setImage(new Image(getClass().getResource("/images/penIcon.png").toExternalForm()));
        penIcon2.setImage(new Image(getClass().getResource("/images/penIcon.png").toExternalForm()));
    }

    public void addItem() {
        String name = itemName.getText();
        String description = itemDescription.getText();
        String category = itemCategory.getValue();
        boolean isArcane = itemArcane.isSelected();

        if (name.isEmpty() || description.isEmpty() || itemGP.getText().isEmpty() || itemSP.getText().isEmpty() || itemCP.getText().isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        int copperValue;
        try {
            copperValue = (Integer.parseInt(itemGP.getText()) * 100) + (Integer.parseInt(itemSP.getText()) * 10) + Integer.parseInt(itemCP.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numeric values for the price.");
            return;
        }

        try {
            storeManager.addProduct(name, description, category, copperValue, isArcane, selectedImagePath);
            showAlert("Success", "Item added successfully.");
            handleClosePopup();
        } catch (RuntimeException | SQLException e) {
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    private void handleAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                File directory = new File("SWEMagicShopProject/src/images/products");
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File copiedFile = new File(directory, selectedFile.getName());

                Files.copy(selectedFile.toPath(), copiedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                selectedImagePath = "/images/products/" + selectedFile.getName();
                imagePathLabel.setText(selectedImagePath);
                itemImage.setImage(new Image(copiedFile.toURI().toString()));

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Something went wrong while selecting your image.");
            }
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
}
