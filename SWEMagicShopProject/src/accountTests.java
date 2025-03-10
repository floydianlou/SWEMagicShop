import BusinessLogic.AccountManager;
import DomainModel.Species;

import java.util.Scanner;


public class accountTests {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountManager accountManager = new AccountManager(); // Assumo che tu abbia già un'istanza gestibile

        System.out.println("Inserisci i dati per creare un account cliente:");

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
            System.out.println("✅ Account creato con successo!");
        } catch (Exception e) {
            System.err.println("❌ Errore nella creazione dell'account: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    }

