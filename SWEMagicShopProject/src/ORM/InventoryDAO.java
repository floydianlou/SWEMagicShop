package ORM;

import DomainModel.Customer;
import DomainModel.Item;
import Exceptions.InventoryExceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public boolean updateInventory(int customerID, ArrayList<Item> orderItems) throws InventoryExceptions.InventoryUpdateException {
        String inventoryQuery = "INSERT INTO \"Inventory\" (customerid, itemid, quantity) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT (customerID, itemID) " +
                "DO UPDATE SET quantity = \"Inventory\".quantity + EXCLUDED.quantity;";

        try (PreparedStatement stmt = connection.prepareStatement(inventoryQuery)) {
            connection.setAutoCommit(false);

            for (Item orderItem : orderItems) {
                stmt.setInt(1, customerID);
                stmt.setInt(2, orderItem.getItemID());
                stmt.setInt(3, orderItem.getItemQuantity());
                stmt.addBatch();
            }

            stmt.executeBatch();
            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new InventoryExceptions.InventoryUpdateException("Rollback failed: " + rollbackEx.getMessage());
            }
            throw new InventoryExceptions.InventoryUpdateException("Failed to update inventory: " + e.getMessage());
        }
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

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet set = stmt.executeQuery()) {
                if (set.next()) {
                    return set.getString("category");
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the hot category: " + e.getMessage());
        }
        return null;
    }

    public String categoryLeastSold() {
        String query = "SELECT c.name AS category " +
                "FROM \"Category\" c " +
                "LEFT JOIN \"Item\" i ON c.categoryID = i.categoryID " +
                "LEFT JOIN \"Inventory\" ivt ON i.itemID = ivt.itemID " +
                "GROUP BY c.name " +
                "ORDER BY COALESCE(SUM(ivt.quantity), 0) ASC " +
                "LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet set = stmt.executeQuery()) {
                if (set.next()) {
                    return set.getString("category");
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the ugly category: " + e.getMessage());
        }
        return null;
    }

    public int numberCategorySold(String category) {
        String query = "SELECT SUM(ivt.quantity) AS total_sold " +
                "FROM \"Inventory\" ivt JOIN \"Item\" i ON ivt.itemID = i.itemID " +
                "JOIN \"Category\" c ON i.categoryID = c.categoryID " +
                "WHERE c.name = ? " +
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

    //ATTENTION: THIS FUNCTION ONLY GIVES YOU ONE RESULT, IF ONE PRODUCTS HAS THE SAME SALES AS ANOTHER IT WON'T SHOW
    public Item productMostSold() {
        String query = "SELECT it.itemid, it.name, it.description, c.name as category, it.arcane, it.cpprice, SUM(i.quantity) AS total_sold " +
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
                    boolean item_arcane = set.getBoolean("arcane");
                    int item_cpprice = set.getInt("cpprice");
                    return new Item(item_id, item_name, item_description, item_category, item_arcane, item_cpprice);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the hot product: " + e.getMessage());
        }
        return null;
    }

    //ATTENTION: THIS FUNCTION ONLY GIVES YOU ONE RESULT, IF ONE PRODUCTS HAS THE SAME SALES AS ANOTHER IT WON'T SHOW
    public Item productLeastSold() {
        String query = "SELECT it.itemID, it.name, it.description, c.name AS category, it.arcane, it.CPprice, " +
                "COALESCE(SUM(inv.quantity), 0) AS total_sold " +
                "FROM \"Item\" it " +
                "LEFT JOIN \"Inventory\" inv ON it.itemID = inv.itemID " +
                "LEFT JOIN \"Category\" c ON it.categoryID = c.categoryID " +
                "GROUP BY it.itemID, it.name, it.description, c.name, it.arcane, it.CPprice " +
                "ORDER BY total_sold ASC " +
                "LIMIT 1";

        try(PreparedStatement stmt = connection.prepareStatement(query)){
            try(ResultSet set = stmt.executeQuery()){
                if(set.next()){
                    int item_id = set.getInt("itemid");
                    String item_name = set.getString("name");
                    String item_description = set.getString("description");
                    String item_category = set.getString("category");
                    boolean item_arcane = set.getBoolean("arcane");
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

        String checkExistenceQuery = "SELECT 1 FROM \"Item\" WHERE itemID = ?";

        String salesQuery = "SELECT COALESCE(SUM(i.quantity), 0) AS total_sold " +
                "FROM \"Inventory\" i " +
                "WHERE i.itemID = ?";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkExistenceQuery)) {
            checkStmt.setInt(1, item);
            try (ResultSet checkSet = checkStmt.executeQuery()) {
                if (!checkSet.next()) {
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while checking item existence: " + e.getMessage());
            return -1;
        }

        try (PreparedStatement stmt = connection.prepareStatement(salesQuery)) {
            stmt.setInt(1, item);
            try (ResultSet set = stmt.executeQuery()) {
                if (set.next()) {
                    return set.getInt("total_sold");
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the number of product sold: " + e.getMessage());
        }
        return 0;
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
        String checkQuery = "SELECT 1 FROM \"Customer\" WHERE customerID = ?";

        String sumQuery = "SELECT COALESCE(SUM(i.quantity * it.CPprice), 0) AS total_spent " +
                "FROM \"Inventory\" i " +
                "JOIN \"Item\" it ON i.itemID = it.itemID " +
                "WHERE i.customerID = ?";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, customer);
            try (ResultSet checkSet = checkStmt.executeQuery()) {
                if (!checkSet.next()) {
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while checking if the customer exists: " + e.getMessage());
            return -1;
        }

        try (PreparedStatement sumStmt = connection.prepareStatement(sumQuery)) {
            sumStmt.setInt(1, customer);
            try (ResultSet sumSet = sumStmt.executeQuery()) {
                if (sumSet.next()) {
                    return sumSet.getInt("total_spent");
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while calculating the total spent: " + e.getMessage());
        }
        return 0;
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

    // FOR GRAPHS
    public Map<String, Integer> getCategorySales() {
        String query = "SELECT c.name AS category, COALESCE(SUM(ivt.quantity), 0) AS total_sold " +
                "FROM \"Category\" c " +
                "LEFT JOIN \"Item\" i ON c.categoryID = i.categoryID " +
                "LEFT JOIN \"Inventory\" ivt ON i.itemID = ivt.itemID " +
                "GROUP BY c.name " +
                "ORDER BY total_sold DESC";

        Map<String, Integer> categorySalesMap = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet set = stmt.executeQuery()) {
                while (set.next()) {
                    String category = set.getString("category");
                    int totalSold = set.getInt("total_sold");
                    categorySalesMap.put(category, totalSold);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving category sales data: " + e.getMessage());
        }

        return categorySalesMap;
    }

    public Map<String, Integer> getItemSales() {
        String query = "SELECT it.name AS item_name, COALESCE(SUM(inv.quantity), 0) AS total_sold " +
                "FROM \"Item\" it " +
                "LEFT JOIN \"Inventory\" inv ON it.itemID = inv.itemID " +
                "LEFT JOIN \"Category\" c ON it.categoryID = c.categoryID " +
                "GROUP BY it.itemID, it.name " +
                "ORDER BY total_sold DESC";

        Map<String, Integer> itemSalesMap = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet set = stmt.executeQuery()) {
                while (set.next()) {
                    String itemName = set.getString("item_name");
                    int totalSold = set.getInt("total_sold");
                    itemSalesMap.put(itemName, totalSold);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving item sales data: " + e.getMessage());
        }

        return itemSalesMap;
    }
}
