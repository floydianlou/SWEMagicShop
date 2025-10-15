import BusinessLogic.CartManager;
import DAO.ConnectionManager;
import GUI.controller.SceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            Main.primaryStage = primaryStage;
            SceneController.setPrimaryStage(primaryStage);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/welcome-view.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Otari's Magic Shop");
            primaryStage.setMaximized(true);
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);

            primaryStage.getIcons().add(new Image(getClass().getResource("/images/shop-icon.png").toExternalForm()));

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        CartManager.resetInstance();
        ConnectionManager.getInstance().closeConnection();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}