package TestSuites;
import BusinessLogic.WalletManager;
import DomainModel.*;
import BusinessLogic.Utilities;
import BusinessLogic.StoreManager;

import java.util.ArrayList;
import java.util.Scanner;


public class itemDAOTestSuite {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StoreManager storeManager = new StoreManager();
        while (true) {
            System.out.println("\n--- Seleziona un'operazione ---");
            System.out.println("1. Aggiungi un prodotto al negozio");
            System.out.println("2. Modifica un prodotto del negozio");
            System.out.println("3. Visualizza tutti i prodotti del negozio");
            System.out.println("4. Ricerca un prodotto del negozio tramite ID");
            System.out.println("5. Ricerca un prodotto del negozio tramite nome");
            System.out.println("6. Ricerca un prodotto del negozio tramite descrizione");
            System.out.println("7. Ricerca un prodotto del negozio tramite categoria");
            System.out.println("8. Ricerca un prodotto del negozio");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1 -> addItemToStore(scanner, storeManager);
                case 2 -> updateItemStore(scanner, storeManager);
                case 3 -> viewAllItems(scanner, storeManager);
                case 4 -> searchByID(scanner, storeManager);
                case 5 -> searchByName(scanner, storeManager);
                case 6 -> searchByDescription(scanner, storeManager);
                case 7 -> searchByCategory(scanner, storeManager);
                case 8 -> search(scanner, storeManager);
                case 0 -> {
                    System.out.println("👋 Uscita dal programma.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("❌ Scelta non valida, riprova.");
            }
        }
    }

    //TODO: correggere testing
    private static void addItemToStore(Scanner scanner, StoreManager storeManager) {
        System.out.println("\n--- Crea un oggetto da aggiungere al negozio ---");

        System.out.print("ID: ");
        int itemID = scanner.nextInt();
        System.out.print("Nome: ");
        String itemName = scanner.nextLine();
        System.out.print("Descrizione: ");
        String itemDescription = scanner.nextLine();
        System.out.print("Categoria: ");
        String itemCategory = scanner.nextLine();
        System.out.print("Arcano: ");
        boolean arcane = scanner.nextBoolean();
        System.out.print("CP: ");
        int itemPrice = scanner.nextInt();
        scanner.nextLine();

        try{
            boolean success = storeManager.addProduct(itemID, itemName, itemDescription, itemCategory, itemPrice, arcane);
            if(success){
                System.out.println("✅ Item aggiunto con successo!");
            } else { System.err.println("❌ Errore nella creazione dell'item!");}
        } catch (Exception e) {
            System.err.println("❌ Errore nella creazione dell'item: " + e.getMessage());
        }
    }
    
    //TODO: correggere testing
    private static void updateItemStore(Scanner scanner, StoreManager storeManager) {
        System.out.println("\n--- Modifica Item ---");
        int itemPrice;
        boolean itemArcane;

        System.out.print("Inserisci l'ID del prodotto che vuoi modificare: ");
        int itemID = scanner.nextInt();

        System.out.println("Lascia vuoto un campo per non modificarlo.");
        System.out.print("Nuovo Nome (attuale: " + storeManager.getProductByID(itemID).getItemName() + "): ");
        String name = scanner.nextLine();
        if (name.isEmpty()) name = storeManager.getProductByID(itemID).getItemName();

        System.out.print("Nuova Descrizione (attuale: " + storeManager.getProductByID(itemID).getItemDescription() + "): ");
        String description = scanner.nextLine();
        if (description.isEmpty()) description = storeManager.getProductByID(itemID).getItemDescription();

        System.out.print("Nuova Categoria (attuale: " + storeManager.getProductByID(itemID).getItemCategory() + "): ");
        String category = scanner.nextLine();
        if (category.isEmpty()) category = storeManager.getProductByID(itemID).getItemCategory();

        System.out.print("Arcano (attuale: " + storeManager.getProductByID(itemID).isArcane() + "): ");
        String arcane = scanner.nextLine();
        if (arcane.isEmpty()){
            itemArcane = storeManager.getProductByID(itemID).isArcane();
        } else {
            itemArcane = Boolean.parseBoolean(arcane);
        }

        System.out.print("Nuovo Prezzo (attuale: " + storeManager.getProductByID(itemID).getCopperValue() + "): ");
        String price = scanner.nextLine();
        if (price.isEmpty()){
            itemPrice = storeManager.getProductByID(itemID).getCopperValue();
        } else {
            itemPrice = Integer.parseInt(price);
        }

        Item updatedItem = new Item(itemID, name, description, category, itemArcane, itemPrice);

        boolean success = storeManager.updateProduct(updatedItem);
        if (success) {
            System.out.println("✅ Prodotto aggiornato con successo!");
        } else {
            System.out.println("❌ Errore durante l'aggiornamento del prodotto.");
        }
    }
    private static void viewAllItems(Scanner scanner, StoreManager storeManager) {
        System.out.println("\n--- Lista Prodotti ---");
        ArrayList<Item> items = storeManager.listProducts();

        if (items.isEmpty()) {
            System.out.println("❌ Nessun prodotto trovato.");
        } else {
            for (Item item : items) {
                System.out.println(item.getData());
                System.out.println("------------------------");
            }
        }
    }
    private static void searchByID(Scanner scanner, StoreManager storeManager) {
        System.out.println("\n--- Ricerca per ID ---");
        System.out.print("Inserisci l'ID del prodotto: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Item item = storeManager.getProductByID(id);
        if (item != null) {
            System.out.println("✅ Prodotto trovato:\n" + item.getData());
        } else {
            System.out.println("❌ Nessun prodotto trovato con questo ID.");
        }
    }
    private static void searchByName(Scanner scanner, StoreManager storeManager) {
        System.out.println("\n--- Ricerca per nome ---");

        System.out.print("Inserisci il nome del prodotto: ");
        String name = scanner.nextLine();
        ArrayList<Item> items = storeManager.searchProducsByName(name);

        if (items.isEmpty()) { System.out.println("❌ Nessun prodotto trovato."); }
        else {
            System.out.println("✅ Prodotto trovato:\n");
            for (Item item : items) {
                System.out.println(item.getData());
                System.out.println("------------------------");
            }
        }
    }
    private static void searchByDescription(Scanner scanner, StoreManager storeManager) {
        System.out.println("\n--- Ricerca per descrizione ---");

        System.out.print("Inserisci la descrizione del prodotto: ");
        String description = scanner.nextLine();
        ArrayList<Item> items = storeManager.searchProductsByDescription(description);

        if (items.isEmpty()) { System.out.println("❌ Nessun prodotto trovato."); }
        else {
            System.out.println("✅ Prodotto trovato:\n");
            for (Item item : items) {
                System.out.println(item.getData());
                System.out.println("------------------------");
            }
        }
    }
    private static void searchByCategory(Scanner scanner, StoreManager storeManager) {
        System.out.println("\n--- Ricerca per categoria ---");

        System.out.print("Inserisci la categoria del prodotto: ");
        String category = scanner.nextLine();
        ArrayList<Item> items = storeManager.searchProductsByCategory(category);

        if (items.isEmpty()) { System.out.println("❌ Nessun prodotto trovato."); }
        else {
            System.out.println("✅ Prodotto trovato:\n");
            for (Item item : items) {
                System.out.println(item.getData());
                System.out.println("------------------------");
            }
        }
    }
    private static void search(Scanner scanner, StoreManager storeManager) {
        System.out.println("\n--- Ricerca Prodotto ---");

        System.out.print("Inserisci per quale criterio vuoi effettuare la tua ricerca (lowercase): ");
        String option = scanner.nextLine();
        System.out.print("Inserisci " + option + " del prodotto: ");
        String search = scanner.nextLine();
        ArrayList<Item> items = storeManager.searchProductsBy(option, search);
        if (items.isEmpty()) { System.out.println("❌ Nessun prodotto trovato."); }
        else {
            System.out.println("✅ Prodotto trovato:\n");
            for (Item item : items) {
                System.out.println(item.getData());
                System.out.println("------------------------");
            }
        }
    }

}
