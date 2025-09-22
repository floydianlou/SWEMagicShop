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
import java.io.IOException;

public class MainViewController {

    public HBox toolBar;
    public Label shoplabel;
    @FXML
    private AnchorPane contentArea;
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

        Person loggedUser = LoggedUserManager.getInstance().getLoggedUser();

        if (loggedUser != null) {
            if (loggedUser instanceof Customer) {
                loadContent("customer-shop-view.fxml");
                updateTopBar("customer");
            } else {
                loadContent("manager-shop-view.fxml");
                updateTopBar("manager");
            }
        }

        shoplabel.setCursor(javafx.scene.Cursor.HAND);
        shoplabel.setFocusTraversable(true);
        shoplabel.setOnMouseClicked(e -> {
            if (e.getButton() == javafx.scene.input.MouseButton.PRIMARY) goHomeForLoggedUser();
        });
        shoplabel.setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ENTER || e.getCode() == javafx.scene.input.KeyCode.SPACE) {
                goHomeForLoggedUser();
            }
        });

    }

    public void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/" + fxmlFile));
            Node content = loader.load();

            switch (fxmlFile) {
                case "customer-shop-view.fxml" -> ((CustomerShopViewController) loader.getController()).setMainViewController(this);
                case "cart-view.fxml" -> ((CartController) loader.getController()).setMainViewController(this);
                case "product-view.fxml" -> ((ProductViewController) loader.getController()).setMainViewController(this);
                case "account-view.fxml" -> ((AccountViewController) loader.getController()).setMainViewController(this);
                case "manager-shop-view.fxml" -> ((ManagerShopController) loader.getController()).setMainViewController(this);
                case "manager-product-view.fxml" -> ((ManagerProductViewController) loader.getController()).setMainViewController(this);
                case "manager-account-view.fxml" -> ((ManagerAccountViewController) loader.getController()).setMainViewController(this);
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

            case "manager" -> {
                Button accountButton = createButton("/images/accountIcon.png", "Account", _ -> {
                    loadContent("manager-account-view.fxml");
                    updateTopBar("managerAccount");
                });

                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> {
                    handleLogout();
                });

                toolBar.getChildren().addAll(createWelcomeLabel(), accountButton, logoutButton);
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

            case "managerAccount" -> {
                Button backButton = createButton("/images/homeIcon.png", "Shop home", _ -> {
                    loadContent("manager-shop-view.fxml");
                    updateTopBar("manager");
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

            case "orderhistory", "wallet", "inventory", "arcane" -> {
                Button backButton = createButton("/images/accountIcon.png", "Back to Account", _ -> {
                    loadContent("account-view.fxml");
                    updateTopBar("account");
                });

                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> {
                    handleLogout();
                });

                toolBar.getChildren().addAll(backButton, logoutButton);
            }

            case "manageStatistics", "manageArcaneRequest" -> {
                Button backButton = createButton("/images/accountIcon.png", "Back to Account", _ -> {
                    loadContent("manager-account-view.fxml");
                    updateTopBar("managerAccount");
                });

                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> {
                    handleLogout();
                });

                toolBar.getChildren().addAll(backButton, logoutButton);
            }

            case "managerProduct" -> {
                Button backButton = createButton("/images/homeIcon.png", "Shop home", _ -> {
                    loadContent("manager-shop-view.fxml");
                    updateTopBar("manager");
                });

                Button accountButton = createButton("/images/accountIcon.png", "Account", _ -> {
                    loadContent("manager-account-view.fxml");
                    updateTopBar("managerAccount");
                });

                Button logoutButton = createButton("/images/logoutIcon.png", "Logout", _ -> {
                    handleLogout();
                });

                toolBar.getChildren().addAll(backButton, accountButton, logoutButton);

            }

        }
        if (LoggedUserManager.getInstance().getLoggedUser() instanceof Customer) {
            updateCartIcon(); }
    }

    // to add red dot on cart icon
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

    // TODO check if it can be made an if-case
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
        CartManager.resetInstance();
        LoggedUserManager.getInstance().clearSession();
        SceneController.loadScene("welcome-view.fxml");
    }

    private void goHomeForLoggedUser() {
        Person currentUser = LoggedUserManager.getInstance().getLoggedUser();
        if (currentUser instanceof Customer) {
            loadContent("customer-shop-view.fxml");
            updateTopBar("customer");
        } else if (currentUser != null) {
            loadContent("manager-shop-view.fxml");
            updateTopBar("manager");
        }
    }

}