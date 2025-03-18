package TestSuites;
import DomainModel.*;
import BusinessLogic.Utilities;
import BusinessLogic.WalletManager;
import ORM.CategoryDAO;

import java.util.ArrayList;
import java.util.Scanner;

public class categoryDAOTestSuite {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CategoryDAO categoryDAO = new CategoryDAO();
        while (true) {
            System.out.println("\n--- Seleziona un'operazione ---");
            System.out.println("1. Visualizza tutte le categorie");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1 -> viewCategory(scanner, categoryDAO);
                case 0 -> {
                    System.out.println("👋 Uscita dal programma.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("❌ Scelta non valida, riprova.");
            }
        }
    }

    private static void viewCategory(Scanner scanner, CategoryDAO categoryDAO) {
        System.out.println("\n--- Lista delle categorie ---");
        try{
            ArrayList<String> categories = categoryDAO.viewAllCategories();
            for (String c : categories) {
                System.out.println(c);
            }
            System.out.println("✅ Categorie visualizzate con successo!");
        } catch (Exception e){
            System.err.println("❌ Errore nella visualizzazione delle categorie: " + e.getMessage());
        }
    }
}
