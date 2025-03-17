package BusinessLogicTests;

import BusinessLogic.ArcaneRequestManager;
import DomainModel.ArcaneRequest;
import DomainModel.Customer;
import DomainModel.Manager;
import DomainModel.Person;
import ORM.AccountDAO;
import javafx.scene.shape.Arc;

import java.util.ArrayList;
import java.util.Scanner;

public class ArcaneDAOTestSuite {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArcaneRequestManager manager = new ArcaneRequestManager();
        AccountDAO accountDAO = new AccountDAO(); // Classe che contiene il metodo loginPerson()

        System.out.println("=== Welcome to the Arcane Request Test Suite ===");

        // LOGIN
        Person user = null;
        while (user == null) {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            user = accountDAO.loginPerson(email, password);

            if (user == null) {
                System.out.println("Invalid email or password. Try again.");
            }
        }

        // Se l'utente è un Manager
        if (user instanceof Manager) {
            System.out.println("\n=== Manager Mode Activated ===");
            managerMenu(scanner, manager);
        }
        // Se l'utente è un Customer
        else if (user instanceof Customer) {
            System.out.println("\n=== Customer Mode Activated ===");
            Customer customer = (Customer) user;
            customerMenu(scanner, manager, customer);
        }

        scanner.close();
    }

    // Menu per i Manager
    private static void managerMenu(Scanner scanner, ArcaneRequestManager manager) {
        while (true) {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("1. View All Arcane Requests");
            System.out.println("2. View Pending Requests");
            System.out.println("3. Approve Request");
            System.out.println("4. Reject Request");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    ArrayList<ArcaneRequest> allRequests = manager.viewAllRequests();
                    for (ArcaneRequest arcaneRequest : allRequests) {
                        System.out.println("REQUEST ID: " + arcaneRequest.getRequestID() + " REQUEST STATUS: " + arcaneRequest.getRequestStatus() +
                                " CUSTOMER ID: " + arcaneRequest.getCustomerID() + " CUSTOMER NAME: " + arcaneRequest.getCustomerName()); }
                    break;
                case 2:
                    ArrayList<ArcaneRequest> pendingRequests = manager.viewPendingRequests();
                    for (ArcaneRequest arcaneRequest : pendingRequests) {
                        System.out.println("REQUEST ID: " + arcaneRequest.getRequestID() + " REQUEST STATUS: " + arcaneRequest.getRequestStatus()); }
                    break;

                case 3:
                    System.out.print("Enter Request ID to approve: ");
                    int approveID = scanner.nextInt();
                    System.out.println("Approving request made by customer: ");
                    int customerID = scanner.nextInt();
                    ArcaneRequest toapprove = new ArcaneRequest(approveID, "Pending", customerID);
                    boolean approveSuccess = manager.approveRequest(toapprove);
                    System.out.println("Approval " + (approveSuccess ? "successful!" : "failed."));
                    break;

                case 4:
                    System.out.print("Enter Request ID to reject: ");
                    int rejectID = scanner.nextInt();
                    System.out.println("Rejecting request made by customer: ");
                    int customerrejectID = scanner.nextInt();
                    ArcaneRequest toreject = new ArcaneRequest(rejectID, "Pending", customerrejectID);
                    boolean rejectSuccess = manager.rejectRequest(toreject);
                    System.out.println("Rejection " + (rejectSuccess ? "successful!" : "failed."));
                    break;

                case 0:
                    System.out.println("Exiting Manager Mode...");
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    // Menu per i Customer
    private static void customerMenu(Scanner scanner, ArcaneRequestManager manager, Customer customer) {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. Make an Arcane Request");
            System.out.println("2. View My Arcane Requests");
            System.out.println("3. View Number of My Requests");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    boolean requestSuccess = manager.makeArcaneRequest(customer);
                    System.out.println("Request " + (requestSuccess ? "successful!" : "failed."));
                    break;

                case 2:
                    ArrayList<ArcaneRequest> requests = manager.viewRequestsByCustomer(customer.getPersonID());
                    System.out.println("Your Requests: ");
                    for (ArcaneRequest arcaneRequest : requests) {
                    System.out.println("REQUEST ID: " + arcaneRequest.getRequestID() + " REQUEST STATUS: " + arcaneRequest.getRequestStatus()); }
                    break;

                case 3:
                    int numRequests = manager.viewNumberOfArcaneRequests(customer.getPersonID());
                    System.out.println("Total Arcane Requests: " + numRequests);
                    break;

                case 0:
                    System.out.println("Exiting Customer Mode...");
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
