package ORM;

import DomainModel.*;

import java.sql.*;
import java.util.ArrayList;

public class AccountDAO {

    private Connection connection;

    public AccountDAO() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException e) {
            System.out.println("Something happened while trying to connect to database..." + e.getMessage());
        }
    }

    public boolean createCustomerAccount(String name, String surname, String email, String password, int age, String phoneNumber, Species species) throws SQLException {
        String sqlCustomer = "INSERT INTO \"Customer\" (name, surname, email, password, age, phone, speciesID) " +
                "VALUES (?,?,?,?,?,?,?) RETURNING customerID;";
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(sqlCustomer)) {
                stmt.setString(1, name);
                stmt.setString(2, surname);
                stmt.setString(3, email);
                stmt.setString(4, password);
                stmt.setInt(5, age);
                stmt.setString(6, phoneNumber);
                stmt.setInt(7, species.getSpeciesID());

                try (ResultSet set = stmt.executeQuery()) {
                    if (set.next()) {
                        int ID = set.getInt("customerID");
                        String sqlWallet = "INSERT INTO \"Wallet\" (customerID) VALUES (?);";

                        try (PreparedStatement walletstmt = connection.prepareStatement(sqlWallet)) {
                            walletstmt.setInt(1, ID);
                            walletstmt.executeUpdate();
                        }
                        connection.commit();
                        System.out.println("Customer account created");
                        return true;
                    }
                }
            }
        } catch (SQLException exception) {
            try {
                connection.rollback();
                System.err.println("Transaction rolled back due to error.");
            } catch (SQLException throwables) {
                System.out.println("Error while doing rollback: " + throwables.getMessage());
            }
            if (exception.getMessage().contains("duplicate key value violates unique constraint")) {
                System.err.println("This mail is already in use for a customer account.");
            } else {
                System.err.println("Customer account creation failed: " + exception.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true); } catch (SQLException e) {
                System.err.println("Error while reactivating auto commit: " + e.getMessage());
            }
        }
        return false;
    }

    public boolean createManagerAccount(String name, String surname, String email, String password) {
        String sqlManager = String.format("INSERT INTO \"Manager\" (name, surname, email, password) " +
                "VALUES ('%s', '%s', '%s', '%s');", name, surname, email, password);

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sqlManager);
            System.out.println("Manager account created");
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                System.err.println("This mail is already in use for a manager account.");
            } else {
                System.err.println("Manager account creation failed: " + e.getMessage());
            }
            return false;
        }
    }

    public Person loginPerson(String email, String password) {
        String sqlManager = String.format("SELECT managerID, name, surname FROM \"Manager\" WHERE email = '%s' and password = '%s'", email, password);

        try (Statement stmt = connection.createStatement()) {

            try (ResultSet set = stmt.executeQuery(sqlManager)) {
            if (set.next()) {
                int ID = set.getInt("managerID");
                String name = set.getString("name");
                String surname = set.getString("surname");
                return new Manager(ID, name, surname, email, password); }
            }

            String sqlCustomer = String.format(
                    "SELECT c.customerID, c.name AS customerName, c.surname, c.age, c.arcanemembership, c.phone, " +
                            "       s.speciesID, s.name AS speciesName, w.cpBalance " +
                            "FROM \"Customer\" c " +
                            "JOIN \"Wallet\" w ON c.customerID = w.customerID " +
                            "JOIN \"Species\" s ON c.speciesID = s.speciesID " +
                            "WHERE c.email = '%s' AND c.password = '%s';",
                    email, password);

            try (ResultSet newSet = stmt.executeQuery(sqlCustomer)) {
                if (newSet.next()) {
                    int ID = newSet.getInt("customerID");
                    String name = newSet.getString("customerName");
                    String surname = newSet.getString("surname");
                    int age = newSet.getInt("age");
                    boolean arcaneMember = newSet.getBoolean("arcanemembership");
                    String phone = newSet.getString("phone");
                    int speciesID = newSet.getInt("speciesID");
                    String speciesName = newSet.getString("speciesName");
                    int cpBalance = newSet.getInt("cpBalance");

                    Wallet wallet = new Wallet(ID, cpBalance);
                    Species species = new Species(speciesID, speciesName);
                    return new Customer(ID, name, surname, email, password, age, phone, arcaneMember, wallet, species);
                }
            }

        } catch (SQLException exception) {
            System.err.println("Customer account login failed" + exception.getMessage());
        }

        System.out.println("Email or password doesn't match.");
        return null;
    }

    public ArrayList<Customer> viewAllCustomers() {
        // TODO CHECK PREPARED STATEMENT POSSIBILITY
        ArrayList<Customer> customers = new ArrayList<>();
        String sqlCustomer = String.format("SELECT c.customerID, c.name AS customerName, c.surname, c.email, c.password, c.age, c.arcanemembership, c.phone, s.speciesID, s.name AS speciesName, w.cpBalance " +
                        "FROM \"Customer\" c " +
                        "JOIN \"Wallet\" w ON c.customerID = w.customerID " +
                        "JOIN \"Species\" s ON c.speciesID = s.speciesID " +
                        "ORDER BY customerID ASC;");

        try (Statement prp = connection.createStatement();
            ResultSet set = prp.executeQuery(sqlCustomer)) {
            while (set.next()) {
                int ID = set.getInt("customerID");
                String name = set.getString("customerName");
                String surname = set.getString("surname");
                String email = set.getString("email");
                String password = set.getString("password");
                int age = set.getInt("age");
                boolean arcaneMember = set.getBoolean("arcanemembership");
                String phone = set.getString("phone");
                int cpBalance = set.getInt("cpBalance");
                int speciesID = set.getInt("speciesID");
                String speciesName = set.getString("speciesName");
                Wallet wallet = new Wallet(ID, cpBalance);
                Species species = new Species(speciesID, speciesName);
                customers.add(new Customer(ID, name, surname, email, password, age, phone, arcaneMember, wallet, species));
            }
        } catch (SQLException exception) {
            System.err.println("Failed to load customer accounts: " + exception.getMessage());
        }
        return customers;
    }

    public Customer getCustomerByID(int customerID) {
        // TODO CHECK PREPARED STATEMENT POSSIBILITY
        Customer customer = null;
        String sqlCustomer = String.format("SELECT c.customerID, c.name AS customerName, c.surname, c.email, c.password, c.age, c.arcanemembership, c.phone, s.speciesID, s.name AS speciesName, w.cpBalance " +
                "FROM \"Customer\" c " +
                "JOIN \"Wallet\" w ON c.customerID = w.customerID " +
                "JOIN \"Species\" s ON c.speciesID = s.speciesID " +
                "WHERE c.customerID = %d;", customerID);

        try (PreparedStatement prp = connection.prepareStatement(sqlCustomer);
             ResultSet set = prp.executeQuery()) {
            if (set.next()) {
                int ID = set.getInt("customerID");
                String name = set.getString("customerName");
                String surname = set.getString("surname");
                String email = set.getString("email");
                String password = set.getString("password");
                int age = set.getInt("age");
                boolean arcaneMember = set.getBoolean("arcanemembership");
                String phone = set.getString("phone");
                int cpBalance = set.getInt("cpBalance");
                int speciesID = set.getInt("speciesID");
                String speciesName = set.getString("speciesName");
                Wallet wallet = new Wallet(ID, cpBalance);
                Species species = new Species(speciesID, speciesName);
                customer = new Customer(ID, name, surname, email, password, age, phone, arcaneMember, wallet, species);
            }
        } catch (SQLException exception) {
            System.err.println("Failed to load customer account: " + exception.getMessage());
        }
        return customer;
    }

    public boolean updateCustomerAccount(Customer customer) {
        // TODO CHECK PREPARED STATEMENT POSSIBILITY
        String sqlUpdate = String.format("UPDATE \"Customer\" SET name = '%s', surname = '%s', email = '%s', password = '%s', phone = '%s' " +
                "WHERE customerID = %d", customer.getName(), customer.getSurname(), customer.getEmail(), customer.getPassword(), customer.getPhoneNumber(),
                customer.getPersonID());

        try (Statement stmt = connection.createStatement()) {
            int affected = stmt.executeUpdate(sqlUpdate);
            if (affected > 0) {
                System.out.println("Customer account updated");
                return true;
            } else {
                System.out.println("No rows were affected.");
            }
        } catch (SQLException exception) {
            System.err.println("Failed to update customer account: " + exception.getMessage());
        }
        return false;
    }

    public boolean updateManagerAccount(Manager manager) {
        // TODO CHECK PREPARED STATEMENT POSSIBILITY
        String sqlUpdate = String.format("UPDATE \"Manager\" SET name = '%s', surname = '%s', email = '%s', password = '%s' " +
                        "WHERE managerID = %d", manager.getName(), manager.getSurname(), manager.getEmail(), manager.getPassword(), manager.getPersonID());

        try (Statement stmt = connection.createStatement()) {
            int affected = stmt.executeUpdate(sqlUpdate);
            if (affected > 0) {
                System.out.println("Manager account updated");
                return true;
            } else {
                System.out.println("No rows were affected.");
            }
        } catch (SQLException exception) {
            System.err.println("Failed to update manager account: " + exception.getMessage());
        }
        return false;
    }

}
