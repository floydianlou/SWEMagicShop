package BusinessLogic;

import DomainModel.Species;

import java.io.File;

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

}