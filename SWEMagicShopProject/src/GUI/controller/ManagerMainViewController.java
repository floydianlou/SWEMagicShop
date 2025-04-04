// File: GUI/controller/ManagerMainViewController.java
package GUI.controller;

import BusinessLogic.CartManager;
import DomainModel.Person;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import java.io.IOException;

public class ManagerMainViewController {

    public HBox toolBar;
    public Label shoplabel;
    protected AnchorPane contentArea;
    private ImageView shopIcon;

    public void initialize() {
        shopIcon = new ImageView();
        shopIcon.setImage(new Image(getClass().getResource("/images/shop-icon.png").toExternalForm()));
        loadContent("manager-shop-view.fxml");
        updateTopBar("manager");
    }

    public void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/" + fxmlFile));
            Node content = loader.load();

            if (fxmlFile.equals("manager-shop-view.fxml")) {
                ManagerShopController managerShopController = loader.getController();
                managerShopController.setMainViewController(this);
            }

            AnchorPane wrapper = new AnchorPane(content);
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
            contentArea.getChildren().setAll(wrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTopBar(String type) {
        toolBar.getChildren().removeIf(node -> node instanceof Button || (node instanceof Label && node != shoplabel));

        switch (type) {
            case "manager" -> {
                Button accountButton = createButton("/images/accountIcon.png", "Account", _ -> {
                    loadContent("manager-account-view.fxml");
                    updateTopBar("account");
                });
                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> handleLogout());
                toolBar.getChildren().addAll(createWelcomeLabel(), accountButton, logoutButton);
            }
            case "account" -> {
                Button backButton = createButton("/images/homeIcon.png", "Shop home", _ -> {
                    loadContent("manager-shop-view.fxml");
                    updateTopBar("manager");
                });
                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> handleLogout());
                toolBar.getChildren().addAll(backButton, logoutButton);
            }
            case "product" -> {
                Button backButton = createButton("/images/homeIcon.png", "Shop home", _ -> {
                    loadContent("manager-shop-view.fxml");
                    updateTopBar("manager");
                });
                Button accountButton = createButton("/images/accountIcon.png", "Account", _ -> {
                    loadContent("manager-account-view.fxml");
                    updateTopBar("account");
                });
                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> handleLogout());
                toolBar.getChildren().addAll(createWelcomeLabel(), backButton, accountButton, logoutButton);
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

    private void handleLogout() {
        Person loggedUser = LoggedUserManager.getInstance().getLoggedUser();
        if (loggedUser != null) {
            CartManager.getInstance().closeCartSession();
        }
        LoggedUserManager.getInstance().clearSession();
        SceneController.loadScene("welcome-view.fxml");
    }
}