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

    public Item getItemByID(int ID) throws RuntimeException {
        Item item = null;
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane, i.imagepath  " +
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
                    String imagePath = resultSet.getString("imagepath");
                    item = new Item(itemID, name, description, category, arcane, CPprice, imagePath);
                }else {
                    throw new RuntimeException("Item ID not found: " + ID);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your item: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
        return item;
    }

    public ArrayList<Item> getItemsByName(String itemName) throws RuntimeException {
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane, i.imagepath " +
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
                    String imagePath = resultSet.getString("imagepath");
                    Item newItem = new Item(itemID, name, description, category, arcane, CPprice, imagePath);
                    items.add(newItem);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your items: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
        return items;
    }

    public ArrayList<Item> getItemsByNameNonArcane(String itemName) throws RuntimeException {
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane, i.imagepath " +
                "FROM \"Item\" i " +
                "JOIN \"Category\" c ON i.categoryid = c.categoryid " +
                "WHERE i.name = ? AND i.arcane = false";
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
                    String imagePath = resultSet.getString("imagepath");
                    Item newItem = new Item(itemID, name, description, category, arcane, CPprice, imagePath);
                    items.add(newItem);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your items: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
        return items;
    }

    public ArrayList<Item> getItemsByDescription(String itemDescription) throws RuntimeException {
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane, i.imagepath " +
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
                    String imagePath = resultSet.getString("imagepath");
                    Item newItem = new Item(itemID, name, description, category, arcane, CPprice, imagePath);
                    items.add(newItem);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your items: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
        return items;
    }

    public ArrayList<Item> getItemsByDescriptionNonArcane(String itemDescription) throws RuntimeException {
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane, i.imagepath " +
                "FROM \"Item\" i " +
                "JOIN \"Category\" c ON i.categoryid = c.categoryid " +
                "WHERE i.description = ? AND i.arcane = false";
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
                    String imagePath = resultSet.getString("imagepath");
                    Item newItem = new Item(itemID, name, description, category, arcane, CPprice, imagePath);
                    items.add(newItem);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your items: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
        return items;
    }

    public ArrayList<Item> getItemsByCategory(String cat) throws RuntimeException {
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane, i.imagepath " +
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
                    String imagePath = resultSet.getString("imagepath");
                    Item newItem = new Item(itemID, name, description, category, arcane, CPprice, imagePath);
                    items.add(newItem);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your items: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
        return items;
    }

    public ArrayList<Item> getItemsByCategoryNonArcane(String cat) throws RuntimeException {
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane, i.imagepath " +
                "FROM \"Item\" i " +
                "JOIN \"Category\" c ON i.categoryid = c.categoryid " +
                "WHERE c.name = ? AND i.arcane = false";
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
                    String imagePath = resultSet.getString("imagepath");
                    Item newItem = new Item(itemID, name, description, category, arcane, CPprice, imagePath);
                    items.add(newItem);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your items: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
        return items;
    }

    public ArrayList<Item> getItemsByArcane(boolean isArcane) throws RuntimeException{
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane, i.imagepath " +
                "FROM \"Item\" i " +
                "JOIN \"Category\" c ON i.categoryid = c.categoryid " +
                "WHERE i.arcane = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setBoolean(1, isArcane);
            try(ResultSet resultSet = stmt.executeQuery()){
                while (resultSet.next()) {
                    int itemID = resultSet.getInt("itemID");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int CPprice = resultSet.getInt("CPprice");
                    String category = resultSet.getString("categoryName");
                    boolean arcane = resultSet.getBoolean("arcane");
                    String imagePath = resultSet.getString("imagepath");
                    Item newItem = new Item(itemID, name, description, category, arcane, CPprice, imagePath);
                    items.add(newItem);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your items: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
        return items;
    }

    public ArrayList<Item> getItemsByPriceRange(int minPrice, int maxPrice) throws RuntimeException{
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane, i.imagepath " +
                "FROM \"Item\" i " +
                "JOIN \"Category\" c ON i.categoryid = c.categoryid " +
                "WHERE i.cpprice BETWEEN ? AND ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, minPrice);
            stmt.setInt(2, maxPrice);
            try(ResultSet resultSet = stmt.executeQuery()){
                while (resultSet.next()) {
                    int itemID = resultSet.getInt("itemID");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int CPprice = resultSet.getInt("CPprice");
                    String category = resultSet.getString("categoryName");
                    boolean arcane = resultSet.getBoolean("arcane");
                    String imagePath = resultSet.getString("imagepath");
                    Item newItem = new Item(itemID, name, description, category, arcane, CPprice, imagePath);
                    items.add(newItem);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your items: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
        return items;
    }

    public ArrayList<Item> getItemsByPriceRangeNonArcane(int minPrice, int maxPrice) throws RuntimeException{
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane, i.imagepath " +
                "FROM \"Item\" i " +
                "JOIN \"Category\" c ON i.categoryid = c.categoryid " +
                "WHERE i.cpprice BETWEEN ? AND ? AND i.arcane = false";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, minPrice);
            stmt.setInt(2, maxPrice);
            try(ResultSet resultSet = stmt.executeQuery()){
                while (resultSet.next()) {
                    int itemID = resultSet.getInt("itemID");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int CPprice = resultSet.getInt("CPprice");
                    String category = resultSet.getString("categoryName");
                    boolean arcane = resultSet.getBoolean("arcane");
                    String imagePath = resultSet.getString("imagepath");
                    Item newItem = new Item(itemID, name, description, category, arcane, CPprice, imagePath);
                    items.add(newItem);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your items: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
        return items;
    }

    public ArrayList<Item> getAllItems() throws RuntimeException{
        ArrayList<Item> items = new ArrayList<>();
        String query = "SELECT i.itemid, i.name, i.description, i.cpprice, c.name as categoryName, i.arcane, i.imagepath " +
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
                    String imagePath = resultSet.getString("imagepath");
                    Item newItem = new Item(itemID, name, description, category, arcane, CPprice, imagePath);
                    items.add(newItem);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your items: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
        return items;
    }

    public void createItem(String itemName, String description, String category, int copperValue, boolean isArcane, String imagePath) throws RuntimeException {
        String query = "INSERT INTO \"Item\" ( name, description, CPprice, categoryID, arcane, imagepath) " +
                "VALUES ( ?, ?, ?, (SELECT categoryID FROM \"Category\" WHERE name = ?), ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, itemName);
            stmt.setString(2, description);
            stmt.setInt(3, copperValue);
            stmt.setString(4, category); // Subquery per categoryID
            stmt.setBoolean(5, isArcane);
            stmt.setString(6, imagePath);

            int rowsInserted = stmt.executeUpdate();
            if( rowsInserted > 0 ){
                System.out.println("Item added successfully.");
            } else {
                throw new RuntimeException("No rows were added.");
            }
        } catch (SQLException e) {
            System.err.println("Error while creating item: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
    }

    public void updateItem(Item item) throws RuntimeException {
        String query = "UPDATE \"Item\" SET name = ?, description = ?, CPprice = ?, categoryID = (SELECT categoryID FROM \"Category\" WHERE name = ?), arcane = ?, imagepath = ? " +
                "WHERE itemid = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, item.getItemName());
            stmt.setString(2, item.getItemDescription());
            stmt.setInt(3, item.getCopperValue());
            stmt.setString(4, item.getItemCategory());
            stmt.setBoolean(5, item.isArcane());
            stmt.setString(6, item.getImagePath());
            stmt.setInt(7, item.getItemID());

            int rowsAffected = stmt.executeUpdate();
            if( rowsAffected > 0 ){
                System.out.println("Item updated");
            } else {
                throw new RuntimeException("No rows were affected.");
            }
        } catch (SQLException e) {
            System.err.println("Something happened while updating your item: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
    }
}