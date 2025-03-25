package GUI.controller;


import DomainModel.Customer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.io.IOException;

public class MainViewController {

    public HBox toolBar;
    public Label shoplabel;
    @FXML
    private AnchorPane contentArea;
    @FXML
    private ImageView shopIcon;

    @FXML
    public void initialize() {
        shopIcon.setImage(new Image(getClass().getResource("/images/shop-icon.png").toExternalForm()));

        loadContent("customer-shop-view.fxml");
        updateTopBar("customer");

    }

    public void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/" + fxmlFile));
            Node content = loader.load();

            if (fxmlFile.equals("customer-shop-view.fxml")) { //TODO manage for other pages
                CustomerShopViewController controller = loader.getController();
                controller.setMainViewController(this);
            }

            AnchorPane wrapper = new AnchorPane(content);
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);

            contentArea.getChildren().setAll(wrapper);

            contentArea.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTopBar(String type) {
        toolBar.getChildren().removeIf(node -> node instanceof Button || (node instanceof Label && node != shoplabel));

        switch (type) {
            case "customer" -> {
                Button accountButton = createButton("/images/accountIcon.png", "Account", _ -> {
                    loadContent("account-view.fxml");
                    updateTopBar("account");
                });

                Button cartButton = createButton("/images/cartIcon.png", "Cart", _ -> {
                    loadContent("cart-view.fxml");
                    updateTopBar("cart");
                });

                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> {
                    SceneController.loadScene("welcome-view.fxml");
                });

                toolBar.getChildren().addAll(createWelcomeLabel(), cartButton, accountButton, logoutButton);
            }

            case "manager" -> { // TODO
                Button accountButton = createButton("/images/accountIcon.png", "Account", _ -> {
                    loadContent("account-view.fxml");
                    updateTopBar("account");
                });

                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> {
                    SceneController.loadScene("welcome-view.fxml");
                });

                toolBar.getChildren().addAll(createWelcomeLabel(), accountButton, logoutButton);
            }

            case "cart", "product" -> {
                Button backButton = createButton("/images/homeIcon.png", "Shop home", _ -> {
                    loadContent("customer-shop-view.fxml");
                    updateTopBar("customer");
                });

                Button accountButton = createButton("/images/accountIcon.png", "Account", _ -> {
                    loadContent("account-view.fxml");
                    updateTopBar("account");
                });

                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> {
                    SceneController.loadScene("welcome-view.fxml");
                });

                toolBar.getChildren().addAll(backButton, accountButton, logoutButton);
            }

            case "account" -> {
                Button backButton = createButton("/images/homeIcon.png", "Shop home", _ -> {
                    loadContent("customer-shop-view.fxml");
                    updateTopBar("customer");
                });

                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> {
                    SceneController.loadScene("welcome-view.fxml");
                });

                toolBar.getChildren().addAll(backButton, logoutButton);
            }

        }
    }

    private Button createButton(String iconPath, String hoverText, EventHandler<ActionEvent> handler) {
        ImageView icon = new ImageView(getClass().getResource(iconPath).toExternalForm());
        icon.setFitWidth(30);
        icon.setFitHeight(30);

        Button button = new Button();
        button.setGraphic(icon);
        button.getStyleClass().add("tool-bar-button");
        Tooltip tooltip = new Tooltip(hoverText);
        Tooltip.install(button, tooltip);
        tooltip.getStyleClass().add("tooltip");
        button.setOnAction(handler);

        return button;
    }

    public Label createWelcomeLabel() {
        Label welcomeLabel = new Label("Welcome, " + LoggedUserManager.getInstance().getLoggedUser().getName() + "!");
        welcomeLabel.getStyleClass().add("welcome-label");
        return welcomeLabel;
    }

    }