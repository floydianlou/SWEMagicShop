package ORM;

import DomainModel.Customer;
import DomainModel.Item;
import Exceptions.InventoryExceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryDAO {

    Connection connection;

    public InventoryDAO() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException e) {
            System.out.println("Something happened while trying to connect to database..." + e.getMessage());
        }
    }

    public ArrayList<Item> getInventory(int customerID) {
        //TODO to be modified it misses join with category name and other things
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemID, i.name, i.description, i.categoryid, inv.quantity, i.arcane, i.cpprice " +
                "FROM \"Inventory\" inv " +
                "JOIN \"Item\" i ON inv.itemID = i.itemID " +
                "WHERE inv.customerID = ?;";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, customerID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int itemID = rs.getInt("itemID");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String category = rs.getString("categoryid");
                int quantity = rs.getInt("quantity");
                boolean isArcane = rs.getBoolean("arcane");
                int copperValue = rs.getInt("cpprice");

                items.add(new Item(itemID, name, description, category, quantity, isArcane, copperValue));
            }

        } catch (SQLException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }

        return items;
    }

    public void viewInventory(int ClientID) {
        // TODO
    }

    public int totalRevenue() {
        String query = "SELECT SUM(i.quantity * it.cpprice) as totalRevenue " +
                "FROM \"Inventory\" i " +
                "JOIN \"Item\" it ON i.itemid = it.itemid";
        int revenue = -1;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet set = stmt.executeQuery()) {
                if (set.next()) {
                    revenue = set.getInt("totalRevenue");
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the revenue: " + e.getMessage());
        }
        return revenue;
    }

    public String categoryMostSold() {
        String query = "SELECT c.name AS category " +
                "FROM \"Inventory\" ivt JOIN \"Item\" i ON ivt.itemID = i.itemID " +
            "JOIN \"Category\" c ON i.categoryID = c.categoryID " +
            "GROUP BY c.name " +
            "ORDER BY SUM(ivt.quantity) DESC " +
            "LIMIT 1";

        String cat = "";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet set = stmt.executeQuery()) {
                if (set.next()) {
                    cat = set.getString("category");
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the hot category: " + e.getMessage());
        }
        return cat;
    }

    public String categoryLeastSold() {
        String query = "SELECT c.name AS category " +
                "FROM \"Inventory\" ivt JOIN \"Item\" i ON ivt.itemID = i.itemID " +
                "JOIN \"Category\" c ON i.categoryID = c.categoryID " +
                "GROUP BY c.name " +
                "ORDER BY SUM(ivt.quantity) ASC " +
                "LIMIT 1";

        String cat = "";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet set = stmt.executeQuery()) {
                if (set.next()) {
                    cat = set.getString("category");
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the ugly category: " + e.getMessage());
        }
        return cat;
    }

    public int numberCategorySold(String category) {
        String query = "SELECT SUM(ivt.quantity) AS total_sold " +
                "FROM \"Inventory\" ivt JOIN \"Item\" i ON ivt.itemID = i.itemID " +
                "JOIN \"Category\" c ON i.categoryID = c.categoryID " +
                "WHERE c.name LIKE ?" +
                "GROUP BY c.name ";

        int total=0;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, category);
            try (ResultSet set = stmt.executeQuery()) {
                if (set.next()) {
                    total = set.getInt("total_sold");
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the number of item sold in the category: " + e.getMessage());
        }
        return total;
    }

    public Item productMostSold() {
        String query = "SELECT it.itemid, it.name, it.description, c.name, it.arcane, it.cpprice, SUM(i.quantity) AS total_sold " +
                "FROM \"Inventory\" i " +
                "JOIN \"Item\" it ON i.itemID = it.itemID " +
                "JOIN \"Category\" c ON it.categoryid = c.categoryid " +
                "GROUP BY it.itemID, it.name, it.description, c.name, it.arcane, it.cpprice " +
                "ORDER BY total_sold DESC " +
                "LIMIT 1";

        try(PreparedStatement stmt = connection.prepareStatement(query)){
            try(ResultSet set = stmt.executeQuery()){
                if(set.next()){
                    int item_id = set.getInt("itemid");
                    String item_name = set.getString("name");
                    String item_description = set.getString("description");
                    String item_category = set.getString("category");
                    Boolean item_arcane = set.getBoolean("arcane");
                    int item_cpprice = set.getInt("cpprice");
                    return new Item(item_id, item_name, item_description, item_category, item_arcane, item_cpprice);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the hot product: " + e.getMessage());
        }
        return null;
    }

    public Item productLeastSold() {
        String query = "SELECT it.itemid, it.name, it.description, c.name, it.arcane, it.cpprice, SUM(i.quantity) AS total_sold " +
                "FROM \"Inventory\" i " +
                "JOIN \"Item\" it ON i.itemID = it.itemID " +
                "JOIN \"Category\" c ON it.categoryid = c.categoryid " +
                "GROUP BY it.itemID, it.name, it.description, c.name, it.arcane, it.cpprice " +
                "ORDER BY total_sold ASC " +
                "LIMIT 1";

        try(PreparedStatement stmt = connection.prepareStatement(query)){
            try(ResultSet set = stmt.executeQuery()){
                if(set.next()){
                    int item_id = set.getInt("itemid");
                    String item_name = set.getString("name");
                    String item_description = set.getString("description");
                    String item_category = set.getString("category");
                    Boolean item_arcane = set.getBoolean("arcane");
                    int item_cpprice = set.getInt("cpprice");
                    return new Item(item_id, item_name, item_description, item_category, item_arcane, item_cpprice);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the hot product: " + e.getMessage());
        }
        return null;
    }

    public int numberProductSold(int item) {
        String query = "SELECT SUM(i.quantity) AS total_sold " +
                "FROM \"Inventory\" i " +
                "JOIN \"Item\" it ON i.itemID = it.itemID " +
                "WHERE i.itemid = ?" +
                "GROUP BY it.itemID";

        int numItems = 0;
        try(PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setInt(1, item);
            try(ResultSet set = stmt.executeQuery()){
                if(set.next()){
                    numItems = set.getInt("total_sold");
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the number of product sold: " + e.getMessage());
        }
        return numItems;
    }

    public Customer biggestSpender() {
        String query = "SELECT c.customerID, c.name, c.surname, c.email, SUM(i.quantity * it.CPprice) AS total_spent " +
                "FROM \"Inventory\" i " +
            "JOIN \"Item\" it ON i.itemID = it.itemID " +
            "JOIN \"Customer\" c ON i.customerID = c.customerID " +
            "GROUP BY c.customerID, c.name, c.surname, c.email " +
            "ORDER BY total_spent DESC " +
            "LIMIT 1";

        try(PreparedStatement stmt = connection.prepareStatement(query)){
            try(ResultSet set = stmt.executeQuery()){
                if(set.next()){
                    int id = set.getInt("customerID");
                    String name = set.getString("name");
                    String surname = set.getString("surname");
                    String email = set.getString("email");
                    int total_spent = set.getInt("total_spent");
                    return new Customer(id,name,surname,email);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the biggest spender: " + e.getMessage());
        }
        return null;
    }

    public int totalSpentByCustomer(int customer) {
        String query = "SELECT SUM(i.quantity * it.CPprice) AS total_spent " +
                "FROM \"Inventory\" i " +
                "JOIN \"Item\" it ON i.itemID = it.itemID " +
                "JOIN \"Customer\" c ON i.customerID = c.customerID " +
                "WHERE c.customerID = ? " +
                "GROUP BY c.customerID";

        int totalSpent = 0; // Inizializza il totale speso a 0
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customer);
            try (ResultSet set = stmt.executeQuery()) {
                if (set.next()) {
                    totalSpent = set.getInt("total_spent");
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the total spent: " + e.getMessage());
        }
        return totalSpent;
    }

    public Customer smallestSpender() {
        String query = "SELECT c.customerID, c.name, c.surname, c.email, SUM(i.quantity * it.CPprice) AS total_spent " +
                "FROM \"Inventory\" i " +
                "JOIN \"Item\" it ON i.itemID = it.itemID " +
                "JOIN \"Customer\" c ON i.customerID = c.customerID " +
                "GROUP BY c.customerID, c.name, c.surname, c.email " +
                "ORDER BY total_spent ASC " +
                "LIMIT 1";

        try(PreparedStatement stmt = connection.prepareStatement(query)){
            try(ResultSet set = stmt.executeQuery()){
                if(set.next()){
                    int id = set.getInt("customerID");
                    String name = set.getString("name");
                    String surname = set.getString("surname");
                    String email = set.getString("email");
                    int total_spent = set.getInt("total_spent");
                    return new Customer(id,name,surname,email);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the biggest spender: " + e.getMessage());
        }
        return null;
    }
}
