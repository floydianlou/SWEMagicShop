import BusinessLogic.AccountManager;
import DomainModel.Customer;
import DomainModel.Manager;
import DomainModel.Person;
import DomainModel.Species;
import BusinessLogic.Utilities;
import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.Scanner;


public class accountTests {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountManager accountManager = new AccountManager();

        while (true) {
            System.out.println("\n--- Seleziona un'operazione ---");
            System.out.println("1. Creare un account Customer");
            System.out.println("2. Creare un account Manager (richiede autorizzazione)");
            System.out.println("3. Effettuare il login");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine(); // Consuma il newline

            switch (scelta) {
                case 1 -> createCustomerAccount(scanner, accountManager);
                case 2 -> createManagerAccount(scanner, accountManager);
                case 3 -> login(scanner, accountManager);
                case 0 -> {
                    System.out.println("👋 Uscita dal programma.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("❌ Scelta non valida, riprova.");
            }
        }
    }

    // Metodo per creare un Customer
    private static void createCustomerAccount(Scanner scanner, AccountManager accountManager) {
        System.out.println("\n--- Creazione Account Customer ---");

        System.out.print("Nome: ");
        String firstName = scanner.nextLine();

        System.out.print("Cognome: ");
        String lastName = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Numero di telefono: ");
        String phone = scanner.nextLine();

        System.out.print("Età: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consuma il newline

        // Assegna automaticamente la specie come "Umano"
        Species species = new Species(1, "Human", 18, 150);

        try {
            accountManager.createCustomerAccount(firstName, lastName, email, password, age, phone, species);
            System.out.println("✅ Account Customer creato con successo!");
        } catch (Exception e) {
            System.err.println("❌ Errore nella creazione dell'account: " + e.getMessage());
        }
    }

    // Metodo per creare un Manager con protezione tramite validManagerPassword()
    private static void createManagerAccount(Scanner scanner, AccountManager accountManager) {
        System.out.println("\n--- Creazione Account Manager ---");

        // 🔹 Verifica della password di protezione
        System.out.print("Inserisci la password di autorizzazione per creare un Manager: ");
        String adminPassword = scanner.nextLine();

        if (!Utilities.validManagerPassword(adminPassword)) {
            System.out.println("❌ Autorizzazione negata. Password errata.");
            return;
        }

        System.out.println("🔹 Autorizzazione concessa!");

        System.out.print("Nome: ");
        String firstName = scanner.nextLine();

        System.out.print("Cognome: ");
        String lastName = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            accountManager.createManagerAccount(firstName, lastName, email, password);
            System.out.println("✅ Account Manager creato con successo!");
        } catch (Exception e) {
            System.err.println("❌ Errore nella creazione dell'account: " + e.getMessage());
        }
    }

    // Metodo per fare il login
    private static void login(Scanner scanner, AccountManager accountManager) {
        System.out.println("\n--- Login ---");

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            Person person = accountManager.login(email, password);
            if (person != null) {
                System.out.println("✅ Login riuscito!");
                if (person instanceof Customer customer) {
                    printCustomerDetails(customer);
                } else if (person instanceof Manager manager) {
                    System.out.println("🔹 Benvenuto, Manager " + manager.getName() + " " + manager.getSurname());
                    managerMenu(scanner, accountManager); // ✅ Nuovo menu per i manager
                }
            } else {
                System.out.println("❌ Credenziali errate. Riprova.");
            }
        } catch (Exception e) {
            System.err.println("❌ Errore durante il login: " + e.getMessage());
        }
    }

    // 🔥 Menu riservato ai Manager
    private static void managerMenu(Scanner scanner, AccountManager accountManager) {
        while (true) {
            System.out.println("\n--- Menu Manager ---");
            System.out.println("1. Visualizza tutti i clienti");
            System.out.println("2. Visualizza un cliente con ID");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine(); // Consuma il newline

            switch (scelta) {
                case 1 -> viewAllCustomers(accountManager);
                case 2 -> getCustomerByID(scanner, accountManager);
                case 0 -> {
                    System.out.println("🔙 Tornando al menu principale...");
                    return;
                }
                default -> System.out.println("❌ Scelta non valida, riprova.");
            }
        }
    }

    // Metodo per visualizzare tutti i clienti
    private static void viewAllCustomers(AccountManager accountManager) {
        System.out.println("\n--- Lista Clienti ---");
        ArrayList<Customer> customers = accountManager.showAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("❌ Nessun cliente trovato.");
        } else {
            for (Customer customer : customers) {
                printCustomerDetails(customer);
                System.out.println("------------------------");
            }
        }
    }

    // Metodo per stampare i dettagli di un Customer
    private static void printCustomerDetails(Customer customer) {
        System.out.println("\n--- Dettagli Account Customer ---");
        System.out.println("🔹 Nome: " + customer.getName());
        System.out.println("🔹 Cognome: " + customer.getSurname());
        System.out.println("🔹 Email: " + customer.getEmail());
        System.out.println("🔹 Età: " + customer.getAge());
        System.out.println("🔹 Numero di telefono: " + customer.getPhoneNumber());
        System.out.println("🔹 Membro Arcano: " + (customer.isArcaneMember() ? "Sì" : "No"));
        System.out.println("🔹 Specie: " + customer.getSpeciesName());
        System.out.println("🔹 Bilancio: " + customer.getWalletBalance() + " CP");
    }

    private static void getCustomerByID(Scanner scanner, AccountManager accountManager) {
        System.out.println("\n--- Recupero Customer per ID ---");
        System.out.print("Inserisci l'ID del Customer: ");
        int customerID = scanner.nextInt();
        scanner.nextLine();
        Customer customer = accountManager.getCustomerByID(customerID);
        if (customer != null) {
            printCustomerDetails(customer);
        } else {
            System.out.println("❌ Nessun cliente trovato con questo ID.");
        }
    }
}

