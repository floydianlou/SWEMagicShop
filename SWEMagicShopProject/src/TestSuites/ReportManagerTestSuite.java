package TestSuites;
import DomainModel.*;
import BusinessLogic.Utilities;
import BusinessLogic.ReportManager;

import java.util.Scanner;

public class ReportManagerTestSuite {
    public static void main(String[] args) {
        ReportManager reportManager = new ReportManager();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Seleziona un'operazione ---");
            System.out.println("1. Visualizza la categoria più gettonata");
            System.out.println("2. Visualizza la categoria meno gettonata");
            System.out.println("3. Visualizza il prodotto più venduto");
            System.out.println("4. Visualizza il prodotto meno venduto");
            System.out.println("5. Visualizza il numero di prodotti venduti per id");
            System.out.println("6. Visualizza il numero di prodotti per categoria");
            System.out.println("7. Visualizza l'utente che ha speso di più");
            System.out.println("8. Visualizza l'utente che ha speso di meno");
            System.out.println("9. Visualizza quanto ha speso un utente per id");
            System.out.println("10. Visualizza l'incasso totale del negozio");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1 -> hotCategory(scanner, reportManager);
                case 2 -> uglyCategory(scanner, reportManager);
                case 3 -> hotProduct(scanner, reportManager);
                case 4 -> uglyProduct(scanner, reportManager);
                case 5 -> viewProductNumById(scanner, reportManager);
                case 6 -> viewProductNumByCategory(scanner, reportManager);
                case 7 -> biggestSpender(scanner, reportManager);
                case 8 -> lowestSpender(scanner, reportManager);
                case 9 -> viewTotalSpentByCustomerId(scanner, reportManager);
                case 10 -> viewRevenue(scanner, reportManager);
                case 0 -> {
                    System.out.println("👋 Uscita dal programma.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("❌ Scelta non valida, riprova.");
            }
        }
    }

    public static void hotCategory(Scanner scanner, ReportManager reportManager) {
        System.out.println("\n--- Hot Categoria ---");
        try{
            String category = reportManager.hotCategory();
            System.out.println("La categoria più gettonata è " + category + " num: " + reportManager.categoryNum(category));
            System.out.println("✅ Categoria trovata con successo!");
        } catch(Exception e){
            System.err.println("❌ Errore nella view: " + e.getMessage());
        }
    }

    public static void uglyCategory(Scanner scanner, ReportManager reportManager) {
        System.out.println("\n--- Ugly Categoria ---");
        try{
            String category = reportManager.uglyCategory();
            System.out.println("La categoria meno gettonata è " + category + " num: " + reportManager.categoryNum(category));
            System.out.println("✅ Categoria trovata con successo!");
        } catch(Exception e){
            System.err.println("❌ Errore nella view: " + e.getMessage());
        }
    }

    public static void hotProduct(Scanner scanner, ReportManager reportManager) {
        System.out.println("\n--- Hot Prodotto ---");
        try{
            Item product = reportManager.hotProduct();
            System.out.println("Il prodotto più gettonato è " + product.getItemName() + " num: " + reportManager.productNumById(product.getItemID()));
            System.out.println("✅ Categoria trovata con successo!");
        } catch(Exception e){
            System.err.println("❌ Errore nella view: " + e.getMessage());
        }
    }

    public static void uglyProduct(Scanner scanner, ReportManager reportManager) {
        System.out.println("\n--- Ugly Prodotto ---");
        try{
            Item product = reportManager.uglyProduct();
            System.out.println("Il prodotto meno gettonato è " + product.getItemName() + " num: " + reportManager.productNumById(product.getItemID()));
            System.out.println("✅ Categoria trovata con successo!");
        } catch(Exception e){
            System.err.println("❌ Errore nella view: " + e.getMessage());
        }
    }

    public static void viewProductNumById(Scanner scanner, ReportManager reportManager) {
        System.out.println("\n--- View Product Num ById ---");
        System.out.print("Inserisci l'ID del prodotto: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        try{
            int number = reportManager.productNumById(id);
            System.out.println("Il prodotto è stato venduto " + number + " volte.");
            System.out.println("✅ Numero trovato con successo!");
        }catch(Exception e){
            System.err.println("❌ Errore nella view: " + e.getMessage());
        }
    }

    public static void viewProductNumByCategory(Scanner scanner, ReportManager reportManager) {
        System.out.println("\n--- View Product Num By Category ---");
        System.out.print("Inserisci la categoria: ");
        String cat = scanner.nextLine();
        try{
            int number = reportManager.categoryNum(cat);
            System.out.println("La categoria scelta ha venduto " + number + " prodotti.");
            System.out.println("✅ Numero trovato con successo!");
        }catch(Exception e){
            System.err.println("❌ Errore nella view: " + e.getMessage());
        }
    }

    public static void biggestSpender(Scanner scanner, ReportManager reportManager) {
        System.out.println("\n--- Biggest Spender ---");
        try{
            Customer c = reportManager.viewBiggestSpender();
            System.out.println("Il cliente che ha pagato di più è " + c.getName() + " " + c.getSurname() + " num: " + reportManager.viewTotalSpentByCustomerId(c.getPersonID()));
            System.out.println("✅ Cliente trovato con successo!");
        } catch(Exception e){
            System.err.println("❌ Errore nella view: " + e.getMessage());
        }
    }

    public static void lowestSpender(Scanner scanner, ReportManager reportManager) {
        System.out.println("\n--- Lowest Spender ---");
        try{
            Customer c = reportManager.viewSmallestSpender();
            System.out.println("Il cliente che ha pagato di meno è " + c.getName() + " " + c.getSurname() + " num: " + reportManager.viewTotalSpentByCustomerId(c.getPersonID()));
            System.out.println("✅ Cliente trovato con successo!");
        } catch(Exception e){
            System.err.println("❌ Errore nella view: " + e.getMessage());
        }
    }

    public static void viewTotalSpentByCustomerId(Scanner scanner, ReportManager reportManager) {
        System.out.println("\n--- View Total Spent By Customer Id ---");
        System.out.print("Inserisci la customer ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        try{
            int number = reportManager.viewTotalSpentByCustomerId(id);
            System.out.println("Il cliente ha speso " + number + " soldi.");
            System.out.println("✅ Numero trovato con successo!");
        }catch(Exception e){
            System.err.println("❌ Errore nella view: " + e.getMessage());
        }
    }

    public static void viewRevenue(Scanner scanner, ReportManager reportManager) {
        System.out.println("\n--- View Revenue ---");
        try{
            int revenue = reportManager.revenue();
            System.out.println("L'incasso del negozio è: " + revenue);
            System.out.println("✅ Numero trovato con successo!");
        }catch(Exception e){
            System.err.println("❌ Errore nella view: " + e.getMessage());
        }
    }

}
