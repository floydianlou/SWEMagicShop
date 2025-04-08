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
            System.out.println("8. Ricerca un prodotto del negozio tramite arcane");
            System.out.println("9. Ricerca un prodotto del negozio tramite prezzo");
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
                case 8 -> searchByArcane(scanner, storeManager);
                case 9 -> searchByPrice(scanner, storeManager);
                case 0 -> {
                    System.out.println("👋 Uscita dal programma.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("❌ Scelta non valida, riprova.");
            }
        }
    }

    private static void addItemToStore(Scanner scanner, StoreManager storeManager) {
        /*System.out.println("\n--- Crea un oggetto da aggiungere al negozio ---");

        System.out.print("Nome: ");
        String itemName = scanner.nextLine();
        System.out.print("Descrizione: ");
        String itemDescription = scanner.nextLine();
        System.out.print("Categoria: ");
        String itemCategory = scanner.nextLine();
        System.out.print("Arcano (true o false): ");
        boolean arcane = scanner.nextBoolean();
        System.out.print("Percorso dell'immagine: "); //TODO: controllare perchè qui non prende il percorso
        String image = scanner.nextLine();
        System.out.print("CP: ");
        int itemPrice = scanner.nextInt();
        scanner.nextLine();

        try{
            boolean success = storeManager.addProduct(itemName, itemDescription, itemCategory, itemPrice, arcane, image);
            if(success){
                System.out.println("✅ Item aggiunto con successo!");
            } else { System.err.println("❌ Errore nella creazione dell'item!");}
        } catch (Exception e) {
            System.err.println("❌ Errore nella creazione dell'item: " + e.getMessage());
        }*/
    }


    private static void updateItemStore(Scanner scanner, StoreManager storeManager) {
        /*System.out.println("\n--- Modifica Item ---");

        System.out.print("Inserisci l'ID del prodotto che vuoi modificare: ");
        int itemID = scanner.nextInt();
        scanner.nextLine();

        Item item = storeManager.getProductByID(itemID);
        if (item == null) {
            System.out.println("❌ Prodotto non trovato.");
            return;
        }

        System.out.println("Lascia vuoto un campo per non modificarlo.");

        System.out.print("Nuovo Nome (attuale: " + item.getItemName() + "): ");
        String name = scanner.nextLine();
        if (name.isEmpty()) name = item.getItemName();

        System.out.print("Nuova Descrizione (attuale: " + item.getItemDescription() + "): ");
        String description = scanner.nextLine();
        if (description.isEmpty()) description = item.getItemDescription();

        System.out.print("Nuova Categoria (attuale: " + item.getItemCategory() + "): ");
        String category = scanner.nextLine();
        if (category.isEmpty()) category = item.getItemCategory();

        System.out.print("Arcano (attuale: " + item.isArcane() + "): ");
        String arcaneInput = scanner.nextLine();
        boolean itemArcane = arcaneInput.isEmpty() ? item.isArcane() : Boolean.parseBoolean(arcaneInput);

        System.out.print("Nuovo Prezzo (attuale: " + item.getCopperValue() + "): ");
        String priceInput = scanner.nextLine();
        int itemPrice = priceInput.isEmpty() ? item.getCopperValue() : Integer.parseInt(priceInput);

        System.out.print("Nuova immagine (attuale: " + item.getImagePath() + "): ");
        String image = scanner.nextLine();
        if (image.isEmpty()) image = item.getImagePath();

        Item updatedItem = new Item(itemID, name, description, category, itemArcane, itemPrice, image);

        boolean success = storeManager.updateProduct(updatedItem);
        if (success) {
            System.out.println("✅ Prodotto aggiornato con successo!");
        } else {
            System.out.println("❌ Errore durante l'aggiornamento del prodotto.");
        }*/
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
        ArrayList<Item> items = storeManager.searchProductsByName(name);

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

    private static void searchByArcane(Scanner scanner, StoreManager storeManager) {
        System.out.println("\n--- Ricerca per arcane ---");

        System.out.print("Inserisci la arcane del prodotto: ");
        String arcane = scanner.nextLine();
        ArrayList<Item> items = storeManager.searchProductsByArcane(arcane);

        if (items.isEmpty()) { System.out.println("❌ Nessun prodotto trovato."); }
        else {
            System.out.println("✅ Prodotto trovato:\n");
            for (Item item : items) {
                System.out.println(item.getData());
                System.out.println("------------------------");
            }
        }
    }

    private static void searchByPrice(Scanner scanner, StoreManager storeManager) {
        System.out.println("\n--- Ricerca per prezzo ---");

        System.out.print("Inserisci il prezzo minimo: ");
        int minPrice = scanner.nextInt();
        System.out.print("Inserisci il prezzo massimo: ");
        int maxPrice = scanner.nextInt();
        ArrayList<Item> items = storeManager.searchProductsByPrice(minPrice, maxPrice);

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