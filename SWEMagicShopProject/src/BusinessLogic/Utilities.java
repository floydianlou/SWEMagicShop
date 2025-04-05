package BusinessLogic;

import DomainModel.Species;
import ORM.CategoryDAO;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Utilities {
    public static boolean checkEmail (String email) {
        String emailChecker = "^[a-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$";
        return email.matches(emailChecker);
    }

    public static boolean checkPassword (String password) {
        String notAllowedChars = "[!#$%^&*()+=\\[\\]{};:'\",<>?/\\\\|]";
        return !password.matches("." + notAllowedChars + ".");
    }

    public static boolean checkAgeLimit (Species species, int age) {
        return age >= species.getLegalAge() && age <= species.getLimitAge();
    }

    public static boolean checkPhone (String number) {
        return number.matches("\\d{10}");
    }

    public static boolean validManagerPassword(String inputPassword) { // static?
        String requiredPassword = "ManagerAccountCreation";
        return inputPassword.equals(requiredPassword);
    }

    public static void initialiseSavesFolder() {
        File saveFiles = new File("data/cart");
        if (!saveFiles.exists() && !saveFiles.mkdirs()) {
            System.out.println("Unable to create folder");
        }
    }

    // Money conversion 10CP = 1SP and 10SP = 1GP
    public static String normalizeCurrency(int copperAmount) {
        int SPbalance = copperAmount / 10;
        copperAmount = copperAmount % 10;

        int GPbalance = SPbalance / 10;
        SPbalance = SPbalance % 10;

        return ("GP:" + GPbalance + "\n SP:" + SPbalance + "\n CP:" + copperAmount);
    }

    public static int[] normalizeCurrencyArray(int copperAmount) {
        int SPbalance = copperAmount / 10;
        copperAmount = copperAmount % 10;

        int GPbalance = SPbalance / 10;
        SPbalance = SPbalance % 10;

        return new int[]{GPbalance, SPbalance, copperAmount};

    }

    // to check if the category selected is inside the database
    public static boolean checkCategory(String category) {
        CategoryDAO categoryDAO = new CategoryDAO();
        ArrayList<String> categories = categoryDAO.viewAllCategories();
        for (String c : categories) {
            if (c.equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }

    //to check if arcane is valid
    public static int checkStringArcane(String arcane) {
        int Arcane;
        if (arcane.equalsIgnoreCase("true")) {
            Arcane = 1;
        } else if (arcane.equalsIgnoreCase("false")) {
            Arcane = 1;
        } else {
            Arcane = 0;
        }
        return Arcane;
    }

    public static int checkBooleanArcane(boolean arcane) {
        if(arcane == true || arcane == false){
            return 1;
        }
        else return 0;
    }

    public static StackPane createRoundedImageBox(String imagePath, double width, double height) {
        StackPane container = new StackPane();
        container.setPrefSize(width, height);
        container.getStyleClass().add("item-image-box");

        Image image = null;
        try {
            image = new Image(Utilities.class.getResource(imagePath).toExternalForm());
        } catch (Exception e) {
            System.err.println("Errore caricamento immagine: " + e.getMessage());
        }

        ImageView imageView = new ImageView(image);
        imageView.setSmooth(true);
        imageView.setPreserveRatio(false);

        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        double cropWidth = Math.min(imageWidth, imageHeight * (width / height));
        double cropHeight = Math.min(imageHeight, imageWidth * (height / width));

        double x = (imageWidth - cropWidth) / 2;
        double y = (imageHeight - cropHeight) / 2;

        Rectangle2D viewport = new Rectangle2D(x, y, cropWidth, cropHeight);
        imageView.setViewport(viewport);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        container.getChildren().add(imageView);

        Rectangle clip = new Rectangle(width, height);
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        container.setClip(clip);

        return container;
    }

    public static String formatOrderDate(String rawDate) {
        try {
            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSSSSS][.SSSSS][.SSSS][.SSS][.SS][.S]");
            DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime parsedDate = LocalDateTime.parse(rawDate, inputFormat);
            return outputFormat.format(parsedDate);
        } catch (Exception e) {
            System.err.println("Data format error: " + rawDate);
            return rawDate;
        }
    }

}