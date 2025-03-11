package ORM;

import DomainModel.*;

import java.sql.*;
import java.util.ArrayList;

public class AccountDAO implements DAOInterface.AccountDAO {

    private Connection connection;

    public AccountDAO() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException e) {
            System.out.println("Something happened while trying to connect to database..." + e.getMessage());
        }
    }

    @Override
    public boolean createCustomerAccount(String name, String surname, String email, String password, int age, String phoneNumber, Species species) throws SQLException {
        String sqlCustomer = String.format(
                "INSERT INTO \"Customer\" (name, surname, email, password, age, phone, speciesID) " +
                        "VALUES ('%s', '%s', '%s', '%s', %d, '%s', %d) RETURNING customerID;",
                name, surname, email, password, age, phoneNumber, species.getSpeciesID());

        try (Statement stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sqlCustomer)) {
            if (set.next()) {
                int ID = set.getInt("customerID");
                String sqlWallet = String.format("INSERT INTO \"Wallet\" (customerID) VALUES (%d);", ID);
                try (PreparedStatement walletstmt = connection.prepareStatement(sqlWallet)) {
                    walletstmt.executeUpdate();
                    System.out.println("Customer account created");
                    return true;}
            }
        } catch (SQLException exception) {
            if (exception.getMessage().contains("duplicate key value violates unique constraint")) {
                System.err.println("This mail is already in use for a customer account.");
            } else {
                System.err.println("Customer account creation failed: " + exception.getMessage());
            } }
        return false;
    }

    @Override
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

    @Override
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

    @Override
    public ArrayList<Customer> viewAllCustomers() {
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

    @Override
    public Customer getCustomerByID(int customerID) {
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

    @Override
    public void updateCustomerAccount(Customer customer) {

    }

    @Override
    public void updateManagerAccount(Manager manager) {

    }

    @Override
    public boolean updateCustomerArcaneStatus(int customerID, boolean status) {
        return false;
    }
}
