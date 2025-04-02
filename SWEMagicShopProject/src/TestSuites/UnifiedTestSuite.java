package TestSuites;

import BusinessLogic.*;
import DomainModel.*;

import java.util.ArrayList;
import java.util.Scanner;

public class UnifiedTestSuite {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AccountManager accountManager = new AccountManager();

        boolean running = true;

        while (running) {
            System.out.println("\n========= MAGIC SHOP TEST SUITE =========");
            System.out.println("1. Crea Account Customer");
            System.out.println("2. Crea Account Manager");
            System.out.println("3. Login");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1 -> createCustomerAccount(scanner, accountManager);
                case 2 -> createManagerAccount(scanner, accountManager);
                case 3 -> login(scanner, accountManager);
                case 0 -> {
                    System.out.println("👋 Uscita dal programma.");
                    running = false;
                }
                default -> System.out.println("❌ Scelta non valida.");
            }
        }

        scanner.close();
    }

    // === ACCOUNT ===
    private static void createCustomerAccount(Scanner scanner, AccountManager accountManager) {
        System.out.println("\n--- Crea Account Customer ---");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Cognome: ");
        String cognome = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Telefono: ");
        String telefono = scanner.nextLine();

        System.out.print("Età: ");
        int eta = scanner.nextInt();
        scanner.nextLine();

        Species species = new Species(1, "Human", 18, 150);

        try {
            accountManager.createCustomerAccount(nome, cognome, email, password, eta, telefono, species);
            System.out.println("✅ Account creato!");
        } catch (Exception e) {
            System.err.println("❌ Errore creazione: " + e.getMessage());
        }
    }

    private static void createManagerAccount(Scanner scanner, AccountManager accountManager) {
        System.out.println("\n--- Crea Account Manager ---");

        System.out.print("Password autorizzazione: ");
        String adminPassword = scanner.nextLine();

        if (!Utilities.validManagerPassword(adminPassword)) {
            System.out.println("❌ Password errata.");
            return;
        }

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Cognome: ");
        String cognome = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            accountManager.createManagerAccount(nome, cognome, email, password);
            System.out.println("✅ Manager creato!");
        } catch (Exception e) {
            System.err.println("❌ Errore creazione: " + e.getMessage());
        }
    }

    private static void login(Scanner scanner, AccountManager accountManager) {
        System.out.println("\n--- Login ---");

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Person person = accountManager.login(email, password);

        if (person == null) {
            System.out.println("❌ Credenziali errate.");
            return;
        }

        System.out.println("✅ Login riuscito!");

        if (person instanceof Customer customer) {
            customerMenu(scanner, accountManager, customer);
        } else if (person instanceof Manager manager) {
            managerMenu(scanner, accountManager, manager);
        }
    }

    // === MENU MANAGER ===
    private static void managerMenu(Scanner scanner, AccountManager accountManager, Manager manager) {
        boolean running = true;

        while (running) {
            System.out.println("\n========= MENU MANAGER =========");
            System.out.println("1. Visualizza tutti i clienti");
            System.out.println("2. Visualizza cliente per ID");
            System.out.println("0. Logout");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1 -> viewAllCustomers(accountManager);
                case 2 -> getCustomerByID(scanner, accountManager);
                case 0 -> running = false;
                default -> System.out.println("❌ Scelta non valida.");
            }
        }
    }

    // === MENU CUSTOMER ===
    private static void customerMenu(Scanner scanner, AccountManager accountManager, Customer customer) {
        CartManager cartManager = new CartManager(customer);
        WalletManager walletManager = new WalletManager();
        InventoryManager inventoryManager = new InventoryManager();
        CustomerOrderManager orderManager = new CustomerOrderManager();

        boolean running = true;

        while (running) {
            System.out.println("\n========= MENU CUSTOMER =========");
            System.out.println("1. Dettagli cliente");
            System.out.println("2. Gestisci carrello");
            System.out.println("3. Gestisci ordini");
            System.out.println("4. Gestisci inventario");
            System.out.println("0. Logout");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1 -> printCustomerDetails(customer);
                case 2 -> cartMenu(scanner, cartManager, walletManager, inventoryManager, orderManager, customer);
                case 3 -> orderMenu(scanner, customer, cartManager, walletManager, inventoryManager, orderManager);
                case 4 -> inventoryMenu(scanner, inventoryManager, customer);
                case 0 -> running = false;
                default -> System.out.println("❌ Scelta non valida.");
            }
        }
    }

    // === CART MENU ===
    private static void cartMenu(Scanner scanner, CartManager cartManager, WalletManager walletManager, InventoryManager inventoryManager, CustomerOrderManager orderManager, Customer customer) {
        boolean running = true;

        while (running) {
            System.out.println("\n========= CARRELLO =========");
            System.out.println("1. Aggiungi prodotto");
            System.out.println("2. Visualizza carrello");
            System.out.println("3. Effettua ordine");
            System.out.println("0. Indietro");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1 -> addItemToCart(scanner, cartManager);
                case 2 -> displayCart(cartManager);
                case 3 -> createOrder(customer, cartManager, walletManager, inventoryManager, orderManager);
                case 0 -> running = false;
                default -> System.out.println("❌ Scelta non valida.");
            }
        }
    }

    // === ORDER MENU ===
    private static void orderMenu(Scanner scanner, Customer customer, CartManager cartManager, WalletManager walletManager, InventoryManager inventoryManager, CustomerOrderManager orderManager) {
        boolean running = true;

        while (running) {
            System.out.println("\n========= ORDINI =========");
            System.out.println("1. Visualizza i tuoi ordini");
            System.out.println("0. Indietro");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1 -> viewOrdersByCustomer(orderManager, customer);
                case 0 -> running = false;
                default -> System.out.println("❌ Scelta non valida.");
            }
        }
    }

    // === INVENTORY MENU ===
    private static void inventoryMenu(Scanner scanner, InventoryManager inventoryManager, Customer customer) {
        boolean running = true;

        while (running) {
            System.out.println("\n========= INVENTARIO =========");
            System.out.println("1. Visualizza inventario");
            System.out.println("2. Aggiungi item");
            System.out.println("3. Rimuovi item");
            System.out.println("0. Indietro");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1 -> viewInventory(inventoryManager, customer);
                case 2 -> addItemToInventory(scanner, inventoryManager, customer);
                case 3 -> removeItemFromInventory(scanner, inventoryManager, customer);
                case 0 -> running = false;
                default -> System.out.println("❌ Scelta non valida.");
            }
        }
    }

    // === UTILITY METHODS ===
    private static void viewInventory(InventoryManager inventoryManager, Customer customer) {
        ArrayList<Item> items = inventoryManager.viewInventory(customer);

        if (items.isEmpty()) {
            System.out.println("❌ Inventario vuoto.");
        } else {
            items.forEach(item -> System.out.println(item.getItemData()));
        }
    }

    private static void addItemToInventory(Scanner scanner, InventoryManager inventoryManager, Customer customer) {
        System.out.print("ID Item: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nome: ");
        String name = scanner.nextLine();

        System.out.print("Descrizione: ");
        String description = scanner.nextLine();

        System.out.print("Categoria: ");
        String category = scanner.nextLine();

        System.out.print("Quantità: ");
        int quantity = scanner.nextInt();

        System.out.print("Arcano (true/false): ");
        boolean isArcane = scanner.nextBoolean();

        System.out.print("Valore (CP): ");
        int cpValue = scanner.nextInt();
        scanner.nextLine();

        Item newItem = new Item(id, name, description, category, quantity, isArcane, cpValue);

        // Aggiunta
        ArrayList<Item> inventoryItem = new ArrayList<>();
        inventoryItem.add(newItem);

        try {
            inventoryManager.updateInventory(inventoryItem, customer);
            System.out.println("✅ Item aggiunto!");
        } catch (Exception e) {
            System.err.println("❌ Errore update: " + e.getMessage());
        }
    }

    private static void removeItemFromInventory(Scanner scanner, InventoryManager inventoryManager, Customer customer) {
        System.out.print("ID Item da rimuovere: ");
        int id = scanner.nextInt();

        ArrayList<Item> inventoryItem = inventoryManager.viewInventory(customer);

        inventoryItem.removeIf(item -> item.getItemID() == id);

        try {
            inventoryManager.updateInventory(inventoryItem, customer);
            System.out.println("✅ Item rimosso!");
        } catch (Exception e) {
            System.err.println("❌ Errore update: " + e.getMessage());
        }
    }

    private static void addItemToCart(Scanner scanner, CartManager cartManager) {
        System.out.print("ID prodotto: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nome prodotto: ");
        String name = scanner.nextLine();

        System.out.print("Quantità: ");
        int quantity = scanner.nextInt();

        cartManager.addItemToCart(new Item(id, name, "N/A", "N/A", quantity, false, 10));
        System.out.println("✅ Aggiunto al carrello!");
    }

    private static void displayCart(CartManager cartManager) {
        ArrayList<Item> cart = cartManager.getCartItems();

        if (cart.isEmpty()) {
            System.out.println("🛒 Carrello vuoto.");
        } else {
            cart.forEach(item -> System.out.println(item.getItemID() + ": " + item.getItemName() + " x" + item.getItemQuantity()));
        }
    }

    private static void createOrder(Customer customer, CartManager cartManager, WalletManager walletManager, InventoryManager inventoryManager, CustomerOrderManager orderManager) {
        try {
            int orderId = orderManager.createOrder(customer, cartManager, walletManager);
            System.out.println("✅ Ordine creato! ID: " + orderId);
        } catch (Exception e) {
            System.err.println("❌ Errore ordine: " + e.getMessage());
        }
    }

    private static void viewOrdersByCustomer(CustomerOrderManager orderManager, Customer customer) {
        ArrayList<Order> orders = orderManager.viewCustomerOrders(customer);

        if (orders.isEmpty()) {
            System.out.println("❌ Nessun ordine.");
        } else {
            orders.forEach(order -> System.out.println("ORDER ID: " + order.getOrderID() + " | ACCOUNT: " + order.getCustomerID() + " (name: " + order.getCustomerName()
                    + " email: " + order.getCustomerEmail() + ") | TOTAL COST: " + order.getTotalCP() + "cp" +
                    " | ORDER STATUS: " +order.getOrderStatus() + " | ORDER DATE: " + order.getOrderDate()));
        }
    }

    private static void viewAllCustomers(AccountManager accountManager) {
        ArrayList<Customer> customers = accountManager.showAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("❌ Nessun cliente trovato.");
        } else {
            customers.forEach(UnifiedTestSuite::printCustomerDetails);
        }
    }

    private static void getCustomerByID(Scanner scanner, AccountManager accountManager) {
        System.out.print("ID cliente: ");
        int id = scanner.nextInt();

        Customer customer = accountManager.getCustomerByID(id);

        if (customer != null) {
            printCustomerDetails(customer);
        } else {
            System.out.println("❌ Cliente non trovato.");
        }
    }

    private static void printCustomerDetails(Customer customer) {
        System.out.println("ID: " + customer.getPersonID());
        System.out.println("Nome: " + customer.getName());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Saldo: " + customer.getWalletBalance() + " CP");
    }
}
