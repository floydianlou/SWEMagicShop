package ORM;
import DomainModel.*;

import java.sql.*;
import java.util.ArrayList;

public class ItemDAO {
    private Connection connection;

    public ItemDAO(){
        try{
            this.connection = ConnectionManager.getInstance().getConnection();;
        }
        catch(SQLException e){
            System.out.println("Something happened while trying to connect to database..." + e.getMessage());
        }
    }

    public Item getItemByID(int ID) {
        Item item = null;
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane  " +
                "FROM \"Item\" i JOIN \"Category\" c ON i.categoryid = c.categoryid " +
                "WHERE i.itemid = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int itemID = resultSet.getInt("itemID");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int CPprice = resultSet.getInt("CPprice");
                    String category = resultSet.getString("categoryName");
                    boolean arcane = resultSet.getBoolean("arcane");
                    item = new Item(itemID, name, description, category, arcane, CPprice);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your item: " + e.getMessage());
        }
        return item;
    }

    public ArrayList<Item> getItemsByName(String itemName) {
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane " +
                "FROM \"Item\" i " +
                "JOIN \"Category\" c ON i.categoryid = c.categoryid " +
                "WHERE i.name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, itemName);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int itemID = resultSet.getInt("itemID");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int CPprice = resultSet.getInt("CPprice");
                    String category = resultSet.getString("categoryName");
                    boolean arcane = resultSet.getBoolean("arcane");
                    Item newItem = new Item(itemID, name, description, category, arcane, CPprice);
                    items.add(newItem);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your items: " + e.getMessage());
        }
        return items;
    }

    public ArrayList<Item> getItemsByDescription(String itemDescription) {
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane " +
                "FROM \"Item\" i " +
                "JOIN \"Category\" c ON i.categoryid = c.categoryid " +
                "WHERE i.description = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, itemDescription);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int itemID = resultSet.getInt("itemID");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int CPprice = resultSet.getInt("CPprice");
                    String category = resultSet.getString("categoryName");
                    boolean arcane = resultSet.getBoolean("arcane");
                    Item newItem = new Item(itemID, name, description, category, arcane, CPprice);
                    items.add(newItem);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your items: " + e.getMessage());
        }
        return items;
    }

    public ArrayList<Item> getItemsByCategory(String cat) {
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane " +
                "FROM \"Item\" i " +
                "JOIN \"Category\" c ON i.categoryid = c.categoryid " +
                "WHERE c.name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cat);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int itemID = resultSet.getInt("itemID");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int CPprice = resultSet.getInt("CPprice");
                    String category = resultSet.getString("categoryName");
                    boolean arcane = resultSet.getBoolean("arcane");
                    Item newItem = new Item(itemID, name, description, category, arcane, CPprice);
                    items.add(newItem);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your items: " + e.getMessage());
        }
        return items;
    }

    public ArrayList<Item> getAllItems() {
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane " +
                "FROM \"Item\" i " +
                "JOIN \"Category\" c ON i.categoryid = c.categoryid ";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int itemID = resultSet.getInt("itemID");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int CPprice = resultSet.getInt("CPprice");
                    String category = resultSet.getString("categoryName");
                    boolean arcane = resultSet.getBoolean("arcane");
                    Item newItem = new Item(itemID, name, description, category, arcane, CPprice);
                    items.add(newItem);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your items: " + e.getMessage());
        }
        return items;
    }

    public boolean createItem(int itemID, String itemName, String description, String category, int copperValue, boolean isArcane) {
        String query = "INSERT INTO \"Item\" (itemid, name, description, CPprice, categoryID, arcane) " +
                "VALUES (?, ?, ?, ?, (SELECT categoryID FROM \"Category\" WHERE name = ?), ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemID);
            stmt.setString(2, itemName);
            stmt.setString(3, description);
            stmt.setInt(4, copperValue);
            stmt.setString(5, category); // Subquery per categoryID
            stmt.setBoolean(6, isArcane);

            int rowsInserted = stmt.executeUpdate();
            if( rowsInserted > 0 ){
                System.out.println("Item added successfully.");
                return true;
            } else {
                System.out.println("No rows were added.");
            }
        } catch (SQLException e) {
            System.err.println("Error while creating item: " + e.getMessage());
        }
        return false;
    }

    public boolean updateItem(Item item) {
        String query = "UPDATE \"Item\" SET name = ?, description = ?, CPprice = ?, categoryID = (SELECT categoryID FROM \"Category\" WHERE name = ?), arcane = ? " +
                "WHERE itemid = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, item.getItemName());
            stmt.setString(2, item.getItemDescription());
            stmt.setInt(3, item.getCopperValue());
            stmt.setString(4, item.getItemCategory());
            stmt.setBoolean(5, item.isArcane());
            stmt.setInt(6, item.getItemID());

            int rowsAffected = stmt.executeUpdate();
            if( rowsAffected > 0 ){
                System.out.println("Item updated");
                return true;
            } else {
                System.out.println("No rows were affected.");
            }
        } catch (SQLException e) {
            System.err.println("Something happened while updating your item: " + e.getMessage());
        }
        return false;
    }
}
