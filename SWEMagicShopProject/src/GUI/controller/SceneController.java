package GUI.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/GUI/view/" + fxmlFile));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}