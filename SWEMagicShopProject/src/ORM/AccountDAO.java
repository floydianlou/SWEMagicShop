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
        String sqlWallet = "INSERT INTO \"Wallet\" (customerID) VALUES (?);";

        try {
            connection.setAutoCommit(false);

            int newCustomerId;
            try (PreparedStatement stmt = connection.prepareStatement(sqlCustomer)) {
                stmt.setString(1, name);
                stmt.setString(2, surname);
                stmt.setString(3, email);
                stmt.setString(4, password);
                stmt.setInt(5, age);
                stmt.setString(6, phoneNumber);
                stmt.setInt(7, species.getSpeciesID());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Failed to create customer: no ID returned.");
                    }
                    newCustomerId = rs.getInt("customerID");
                }
            }

            try (PreparedStatement w = connection.prepareStatement(sqlWallet)) {
                w.setInt(1, newCustomerId);
                w.executeUpdate();
            }

            connection.commit();
            System.out.println("Customer account created");
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }

            if ("23505".equals(e.getSQLState())) {
                String constraint = null;
                if (e instanceof org.postgresql.util.PSQLException psql) {
                    var sem = psql.getServerErrorMessage();
                    if (sem != null) constraint = sem.getConstraint();
                }

                if ("unique_email".equals(constraint) || (e.getMessage() != null && e.getMessage().toLowerCase().contains("(email)"))) {
                    throw new SQLException("This email is already in use.", "23505", e);
                } else if ("unique_phone".equals(constraint) || (e.getMessage() != null && e.getMessage().toLowerCase().contains("(phone)"))) {
                    throw new SQLException("This phone number is already in use.", "23505", e);
                } else {
                    throw new SQLException("A unique constraint was violated.", "23505", e);
                }
            }

            throw e;

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public boolean createManagerAccount(String name, String surname, String email, String password) throws SQLException {
        final String sql = "INSERT INTO \"Manager\" (name, surname, email, password) VALUES (?,?,?,?)";

        boolean oldAutoCommit = connection.getAutoCommit();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            ps.setString(1, name);
            ps.setString(2, surname);
            ps.setString(3, email);
            ps.setString(4, password);

            ps.executeUpdate();
            connection.commit();
            System.out.println("Manager account created");
            return true;

        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignore) {}

            if ("23505".equals(e.getSQLState())) {
                String constraint = null;
                if (e instanceof org.postgresql.util.PSQLException psql && psql.getServerErrorMessage() != null) {
                    constraint = psql.getServerErrorMessage().getConstraint();
                }
                if ("unique_manager_email".equals(constraint)
                        || (e.getMessage() != null && e.getMessage().toLowerCase().contains("(email)"))) {
                    throw new SQLException("This email is already in use for a manager account.", "23505", e);
                } else {
                    throw new SQLException("A unique constraint was violated.", "23505", e);
                }
            }
            throw e;

        } finally {
            try { connection.setAutoCommit(oldAutoCommit); } catch (SQLException ignore) {}
        }
    }

    public Person loginPerson(String email, String password) throws SQLException {
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

        }
        return null;
    }

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

    public Customer updateCustomerAccount(int customerid, String name, String surname,
                                          String email, String phone, String password) throws RuntimeException {
        String sqlUpdate = """
        UPDATE "Customer"
        SET name = ?, surname = ?, email = ?, password = ?, phone = ?
        WHERE customerID = ?
    """;

        try (PreparedStatement up = connection.prepareStatement(sqlUpdate)) {
            up.setString(1, name);
            up.setString(2, surname);
            up.setString(3, email);
            up.setString(4, password);
            up.setString(5, phone);
            up.setInt(6, customerid);

            int affected = up.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("No rows were affected (invalid personID).");
            }

            String sqlSelect = """
            SELECT c.customerID, c.customerid, c.name, c.surname, c.email, c.password, c.phone, c.age,
                   c.arcanemembership,
                   s.speciesID, s.name AS species_name
            FROM "Customer" c
            LEFT JOIN "Species" s ON s.speciesID = c.speciesID
            WHERE c.customerid = ?
        """;
            try (PreparedStatement sp = connection.prepareStatement(sqlSelect)) {
                sp.setInt(1, customerid);
                try (ResultSet rs = sp.executeQuery()) {
                    if (!rs.next()) return null;

                    Customer refreshed = new Customer(rs.getInt("customerid"));
                    refreshed.setName(rs.getString("name"));
                    refreshed.setSurname(rs.getString("surname"));
                    refreshed.setEmail(rs.getString("email"));
                    refreshed.setPassword(rs.getString("password"));
                    refreshed.setPhoneNumber(rs.getString("phone"));
                    refreshed.setAge(rs.getInt("age"));
                    refreshed.setArcaneMember(rs.getBoolean("arcanemembership"));

                    int speciesId = rs.getInt("speciesID");
                    if (!rs.wasNull()) {
                        Species spc = new Species(speciesId, rs.getString("species_name"));
                        refreshed.setOwnSpecies(spc);
                    }
                    return refreshed;
                }
            }

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                String msg = e.getMessage();
                if (msg != null) {
                    if (msg.contains("unique_email"))  throw new IllegalArgumentException("Email already in use.");
                    if (msg.contains("unique_phone"))  throw new IllegalArgumentException("Phone number already in use.");
                }
            }
            if (e.getMessage() != null && e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
            throw new RuntimeException("A database error occurred: " + e.getMessage());
        }
    }

    public boolean updateManagerAccount(Manager manager) {
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

    public ArrayList<Species> getAllSpecies () throws SQLException {
        String  speciesSql = "SELECT speciesid, name, adultage, limitage" +
                " FROM \"Species\"";
        ArrayList<Species> species = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(speciesSql)) {
            try (ResultSet set = stmt.executeQuery()) {
                while (set.next()) {
                    int speciesID = set.getInt("speciesid");
                    String name = set.getString("name");
                    int adultage = set.getInt("adultage");
                    int limitage = set.getInt("limitage");
                    species.add(new Species(speciesID, name, adultage, limitage));
                }
            }
        }
        return species;
    }

}