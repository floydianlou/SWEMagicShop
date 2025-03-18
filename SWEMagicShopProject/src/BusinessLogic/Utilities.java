package BusinessLogic;

import DomainModel.Species;
import ORM.CategoryDAO;

import java.io.File;
import java.util.ArrayList;

public class Utilities {
    public static boolean checkEmail (String email) {
        String emailChecker = "^[a-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$";
        return email.matches(emailChecker);
    }

    public static boolean checkPassword (String password) {
        String notAllowedChars = "[!#$%^&*()+=\\[\\]{};:'\",<>?/\\\\|]";
        return !password.matches(".*" + notAllowedChars + ".*");
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
}