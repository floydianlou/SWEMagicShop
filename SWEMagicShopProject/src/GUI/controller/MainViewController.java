package GUI.controller;


import BusinessLogic.CartManager;
import DomainModel.Customer;
import DomainModel.Person;
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
    protected AnchorPane contentArea;
    @FXML
    private ImageView shopIcon;
    @FXML
    private Image emptyCartIcon= new Image(getClass().getResource("/images/cartIcon.png").toExternalForm());;
    @FXML
    private Image fullCartIcon= new Image(getClass().getResource("/images/cartNotificationIcon.png").toExternalForm());;
    @FXML
    private ImageView cartIcon= new ImageView(emptyCartIcon);

    @FXML
    public void initialize() {
        shopIcon.setImage(new Image(getClass().getResource("/images/shop-icon.png").toExternalForm()));
        loadContent("customer-shop-view.fxml");
        updateTopBar("customer");

    }

    // TODO when all pages are connected, better make this function work without all these ifs
    public void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/" + fxmlFile));
            Node content = loader.load();

            if (fxmlFile.equals("customer-shop-view.fxml")) {
                CustomerShopViewController controller = loader.getController();
                controller.setMainViewController(this);
            }
            if (fxmlFile.equals("cart-view.fxml")) {
                CartController cartController = loader.getController();
                cartController.setMainViewController(this);
            }
            if (fxmlFile.equals("product-view.fxml")) {
                ProductViewController productController = loader.getController();
                productController.setMainViewController(this);
            }
            if (fxmlFile.equals("account-view.fxml")) {
                AccountViewController controller = loader.getController();
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

                Button cartButton = createButton(cartIcon, "Cart", _ -> {
                    loadContent("cart-view.fxml");
                    updateTopBar("cart");
                });

                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> {
                    handleLogout();
                });

                toolBar.getChildren().addAll(createWelcomeLabel(), cartButton, accountButton, logoutButton);
            }

            case "manager" -> { // TODO
                Button accountButton = createButton("/images/accountIcon.png", "Account", _ -> {
                    loadContent("account-view.fxml");
                    updateTopBar("account");
                });

                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> {
                    handleLogout();
                });

                toolBar.getChildren().addAll(createWelcomeLabel(), accountButton, logoutButton);//TODO change this
            }

            case "cart" -> {
                Button backButton = createButton("/images/homeIcon.png", "Shop home", _ -> {
                    loadContent("customer-shop-view.fxml");
                    updateTopBar("customer");
                });

                Button accountButton = createButton("/images/accountIcon.png", "Account", _ -> {
                    loadContent("account-view.fxml");
                    updateTopBar("account");
                });

                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> {
                    handleLogout();
                });

                toolBar.getChildren().addAll(backButton, accountButton, logoutButton);
            }

            case "account" -> {
                Button backButton = createButton("/images/homeIcon.png", "Shop home", _ -> {
                    loadContent("customer-shop-view.fxml");
                    updateTopBar("customer");
                });

                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> {
                    handleLogout();
                });

                toolBar.getChildren().addAll(backButton, logoutButton);
            }

            case "product" -> {
                Button backButton = createButton("/images/homeIcon.png", "Shop home", _ -> {
                    loadContent("customer-shop-view.fxml");
                    updateTopBar("customer");
                });

                Button accountButton = createButton("/images/accountIcon.png", "Account", _ -> {
                    loadContent("account-view.fxml");
                    updateTopBar("account");
                });

                Button cartButton = createButton(cartIcon, "Cart", _ -> {
                    loadContent("cart-view.fxml");
                    updateTopBar("cart");
                });

                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> {
                    handleLogout();
                });

                toolBar.getChildren().addAll(createWelcomeLabel(), backButton, cartButton, accountButton, logoutButton);
            }

            case "orderhistory" -> {
                Button backButton = createButton("/images/accountIcon.png", "Back to Account", _ -> {
                    loadContent("account-view.fxml");
                    updateTopBar("account");
                });

                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> {
                    handleLogout();
                });

                toolBar.getChildren().addAll(backButton, logoutButton);
            }

            //TODO

        }
        updateCartIcon();
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

    private Button createButton(ImageView icon, String hoverText, EventHandler<ActionEvent> handler) {
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

    public void updateCartIcon() {
        if (CartManager.getInstance().getCartItems().isEmpty()) {
            cartIcon.setImage(emptyCartIcon);
            cartIcon.setFitWidth(30);
            cartIcon.setFitHeight(30);
        } else {
            cartIcon.setImage(fullCartIcon);
            cartIcon.setFitWidth(30);
            cartIcon.setFitHeight(30);
        }
    }

    public Label createWelcomeLabel() {
        Label welcomeLabel = new Label("Welcome, " + LoggedUserManager.getInstance().getLoggedUser().getName() + "!");
        welcomeLabel.getStyleClass().add("welcome-label");
        return welcomeLabel;
    }

    private void handleLogout() {
        Person loggedUser = LoggedUserManager.getInstance().getLoggedUser();
        if (loggedUser instanceof Customer) {
            CartManager.getInstance().closeCartSession();
        }

        LoggedUserManager.getInstance().clearSession();


        SceneController.loadScene("welcome-view.fxml");
    }

}