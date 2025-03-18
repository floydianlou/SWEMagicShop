package TestSuites;
import DomainModel.*;
import BusinessLogic.Utilities;
import BusinessLogic.WalletManager;

import java.util.Scanner;

public class walletDAOTestSuite {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WalletManager walletManager = new WalletManager();
        while (true) {
            System.out.println("\n--- Seleziona un'operazione ---");
            System.out.println("1. Ricarica il tuo wallet");
            System.out.println("2. Preleva dal tuo wallet");
            System.out.println("3. Visualizza il bilancio del tuo wallet");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1 -> addFundsToWallet(scanner, walletManager);
                case 2 -> withdrawFundsToWallet(scanner, walletManager);
                case 3 -> viewBalance(scanner, walletManager);
                case 0 -> {
                    System.out.println("👋 Uscita dal programma.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("❌ Scelta non valida, riprova.");
            }
        }
    }

    private static void addFundsToWallet(Scanner scanner, WalletManager walletManager) {
        System.out.println("\n-------- Ricarica il tuo wallet -------");

        System.out.println("Inserisci il tuo ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Inserisci quanti soldi vuoi aggiungere nel tuo wallet:\n ");
        System.out.println("GP: ");
        int gp= scanner.nextInt();
        System.out.println("SP: ");
        int sp= scanner.nextInt();
        System.out.println("CP: ");
        int cp= scanner.nextInt();
        scanner.nextLine();

        try{
            walletManager.addFunds(gp,sp,cp,id);
            System.out.println("✅ Wallet ricaricato con successo!");
        } catch (Exception e){
            System.err.println("❌ Errore nella ricarica del tuo wallet: " + e.getMessage());
        }
    }

    private static void withdrawFundsToWallet(Scanner scanner, WalletManager walletManager) {
        System.out.println("\n-------- Prelievo il tuo wallet -------");

        System.out.println("Inserisci il tuo ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Inserisci quanti soldi vuoi prelevare dal tuo wallet:\n ");
        System.out.println("CP: ");
        int cp= scanner.nextInt();
        scanner.nextLine();

        try{
            walletManager.withdrawFunds(cp, id);
            System.out.println("✅ Wallet prelevato con successo!");
        } catch (Exception e) {
            System.err.println("❌ Errore nel prelievo del tuo wallet: " + e.getMessage());
        }
    }

    private static void viewBalance(Scanner scanner, WalletManager walletManager) {
        System.out.println("\n-------- Visualizza il tuo wallet -------");
        System.out.println("Inserisci il tuo ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try{
            System.out.println("Il tuo bilancio è: " + walletManager.viewBalance(id));
            System.out.println("✅ Wallet visualizzato con successo!");
        } catch (Exception e) {
            System.err.println("❌ Errore nella visualizzazione del tuo wallet: " + e.getMessage());
        }

    }

}
