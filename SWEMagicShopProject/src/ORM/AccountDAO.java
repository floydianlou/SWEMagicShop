package ORM;

import DomainModel.Customer;
import DomainModel.Manager;
import DomainModel.Person;
import DomainModel.Species;

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
    public void createCustomerAccount(String name, String surname, String email, String password, int age, String phoneNumber, Species species) throws SQLException {
        String sqlCustomer = String.format(
                "INSERT INTO \"Customer\" (name, surname, email, password, age, phone, speciesID) " +
                        "VALUES ('%s', '%s', '%s', '%s', %d, '%s', %d) RETURNING customerID;",
                name, surname, email, password, age, phoneNumber, species.getSpeciesID());

        Statement stmt = null;
        ResultSet set  = null;
        PreparedStatement walletstmt = null;
        try {
            stmt = connection.createStatement();
            set = stmt.executeQuery(sqlCustomer);

            if (set.next()) {
                int ID = set.getInt("customerID");
                String sqlWallet = String.format("INSERT INTO \"Wallet\" (customerID) VALUES (%d);", ID);
                walletstmt = connection.prepareStatement(sqlWallet);
                walletstmt.executeUpdate();
                System.out.println("Customer account created"); }

        } catch (SQLException exception) {
            System.err.println("Customer account creation failed" + exception.getMessage());
        } finally {
            if (stmt != null) stmt.close();
            if (set != null) set.close();
            if (walletstmt != null) walletstmt.close();
        }
    }

    @Override
    public void createManagerAccount(String name, String surname, String email, String password) {

    }

    @Override
    public Person loginPerson(String email, String password) {
        return null;
    }

    @Override
    public ArrayList<Customer> viewAllCustomers() {
        return null;
    }

    @Override
    public Customer getCustomerByID(int customerID) {
        return null;
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
