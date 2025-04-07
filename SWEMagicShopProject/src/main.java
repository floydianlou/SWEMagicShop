import BusinessLogic.CartManager;
import GUI.controller.SceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            main.primaryStage = primaryStage;
            SceneController.setPrimaryStage(primaryStage);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/welcome-view.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Otari's Magic Shop");
            primaryStage.setMaximized(true);
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);

            // cart thread for autosave closure
            primaryStage.setOnCloseRequest(event -> {
                if (CartManager.getInstance() != null) {
                    CartManager.getInstance().closeCartSession();
                }
                Platform.exit();
                System.exit(0);
            });

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}