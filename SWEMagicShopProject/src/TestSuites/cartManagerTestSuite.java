package TestSuites;

import BusinessLogic.AccountManager;
import BusinessLogic.CartManager;
import DomainModel.Customer;
import DomainModel.Item;
import ORM.AccountDAO;

import java.util.Scanner;

public class cartManagerTestSuite {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountManager accountManager = new AccountManager();
        Customer suiteCustomer = login(scanner, accountManager);
        if (suiteCustomer == null) return;

        CartManager cartManager = new CartManager(suiteCustomer);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Menu Carrello ---");
            System.out.println("1. Aggiungi un prodotto al carrello");
            System.out.println("2. Aumenta quantità di un prodotto");
            System.out.println("3. Riduci quantità di un prodotto");
            System.out.println("4. Rimuovi un prodotto");
            System.out.println("5. Mostra il carrello");
            System.out.println("6. Svuota il carrello");
            System.out.println("7. Salva e chiudi sessione");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    addItemToCart(scanner, cartManager);
                    break;
                case 2:
                    modifyCartQuantity(scanner, cartManager, true);
                    break;
                case 3:
                    modifyCartQuantity(scanner, cartManager, false);
                    break;
                case 4:
                    removeItemFromCart(scanner, cartManager);
                    break;
                case 5:
                    displayCart(cartManager);
                    break;
                case 6:
                    cartManager.clearCart();
                    System.out.println("Carrello svuotato.");
                    break;
                case 7:
                    cartManager.closeCartSession();
                    System.out.println("Carrello salvato. Uscita.");
                    running = false;
                    break;
                default:
                    System.out.println("Scelta non valida.");
            }
        }

        scanner.close();
    }

    private static Customer login(Scanner scanner, AccountManager accountManager) {
        System.out.println("Benvenuto! Effettua il login.");
        System.out.print("Inserisci email: ");
        String email = scanner.nextLine();
        System.out.print("Inserisci password: ");
        String password = scanner.nextLine();

        Customer customer = (Customer) accountManager.login(email, password);
        if (customer == null) {
            System.out.println("Login fallito. Controlla le credenziali.");
            return null;
        }

        System.out.println("Login riuscito! Bentornato, " + customer.getName() + ", " + customer.getEmail());
        return customer;
    }

    private static void addItemToCart(Scanner scanner, CartManager cartManager) {
        System.out.println("Inserisci un prodotto al carrello: ");
        System.out.print("ID prodotto: ");
        int ID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nome prodotto: ");
        String name = scanner.nextLine();
        System.out.print("Inserisci descrizione prodotto: ");
        String description = scanner.nextLine();
        System.out.print("Inserisci categoria prodotto: ");
        String category = scanner.nextLine();
        System.out.print("Inserisci quantità: ");
        int quantity = scanner.nextInt();
        System.out.print("È un oggetto arcano? (true/false): ");
        boolean isArcane = scanner.nextBoolean();
        System.out.print("Inserisci valore in rame: ");
        int copperValue = scanner.nextInt();

        cartManager.addItemToCart(new Item(ID, name, description, category, quantity, isArcane, copperValue));
        System.out.println("Prodotto aggiunto!");
    }

    private static void modifyCartQuantity(Scanner scanner, CartManager cartManager, boolean increase) {
        System.out.print("Inserisci ID prodotto: ");
        int productId = scanner.nextInt();

        Item item = cartManager.getCartItems().stream()
                .filter(i -> i.getItemID() == productId)
                .findFirst()
                .orElse(null);

        if (item != null) {
            if (increase) {
                cartManager.increaseItemQuantity(item);
                System.out.println("Quantità aumentata!");
            } else {
                cartManager.reduceItemQuantity(item);
                System.out.println("Quantità ridotta!");
            }
        } else {
            System.out.println("Prodotto non trovato nel carrello.");
        }
    }

    private static void removeItemFromCart(Scanner scanner, CartManager cartManager) {
        System.out.print("Inserisci ID prodotto da rimuovere: ");
        int removeId = scanner.nextInt();
        cartManager.removeItemFromCart(new Item(removeId, "", "", "", 0, false, 0));
        System.out.println("Prodotto rimosso!");
    }

    private static void displayCart(CartManager cartManager) {
        System.out.println("Carrello attuale:");
        for (Item item : cartManager.getCartItems()) {
            System.out.println(item.getItemID() + " - " + item.getItemName() + " x" + item.getItemQuantity() + " (" + item.getItemDescription() + ")");
        }
    }
}

