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

}
