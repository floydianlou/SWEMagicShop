package ORM;
import java.sql.*;
import java.util.ArrayList;
import DomainModel.Item;
import DomainModel.Order;
import Exceptions.OrderExceptions;

public class OrderDAO {

    private Connection connection;

    public OrderDAO() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException e) {
            System.out.println("Something happened while trying to connect to database..." + e.getMessage());
        }
    }

    public ArrayList<Order> viewAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        String ordersql = "SELECT o.orderid, o.customerid, c.name, c.email, o.orderdate, s.name AS status_name, o.totalcp " +
                "FROM \"Order\" o " +
                "JOIN \"OrderStatus\" s ON o.statusid = s.statusid " +
                "JOIN \"Customer\" c ON o.customerid = c.customerid;";
        try (PreparedStatement stmt = connection.prepareStatement(ordersql)) {
             try (ResultSet set = stmt.executeQuery()) {
                while (set.next()) {
                int orderid = set.getInt("orderid");
                int customerid = set.getInt("customerid");
                String name = set.getString("name");
                String email = set.getString("email");
                String orderdate = set.getString("orderdate");
                String status = set.getString("status_name");
                int totalcp = set.getInt("totalcp");
                Order newOrder = new Order(orderid, status, totalcp, orderdate, customerid, name, email);
                orders.add(newOrder);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your orders: " + e.getMessage());
        }
        return orders;
    }

    public ArrayList<Order> getCustomerOrders(int customerID) {
        ArrayList<Order> orders = new ArrayList<>();
        String ordersql = "SELECT o.orderid, o.orderdate, s.name, o.totalcp " +
                "FROM \"Order\" o JOIN \"OrderStatus\" s ON o.statusid = s.statusid " +
                "WHERE o.customerID = ? ORDER BY o.orderdate ASC;";
        try (PreparedStatement stmt = connection.prepareStatement(ordersql)) {
            stmt.setInt(1, customerID);
            try (ResultSet set = stmt.executeQuery()) {
                while (set.next()) {
                    int orderid = set.getInt("orderid");
                    String orderdate = set.getString("orderdate");
                    String status = set.getString("name");
                    int totalcp = set.getInt("totalcp");
                    Order newOrder = new Order(orderid, status, totalcp, orderdate);
                    orders.add(newOrder);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your orders: " + e.getMessage());
        }
        return orders;
    }

    public int saveNewOrder(int orderTotal, int customerID, ArrayList<Item> cartItems) throws OrderExceptions.OrderSaveException {
        String ordersql = "INSERT INTO \"Order\" (customerid, statusid, totalcp) VALUES (?, 1, ?) RETURNING orderid;";

        // order creation in table Order
        try (PreparedStatement statement = connection.prepareStatement(ordersql)) {
            connection.setAutoCommit(false);
            statement.setInt(1, customerID);
            statement.setInt(2, orderTotal);
            ResultSet set = statement.executeQuery();
            if (!set.next()) {
                throw new SQLException("Failed to save new order: no ID returned.");
            }
            int orderid = set.getInt("orderid");

            // Saving cart items in table OrderItems
            String itemsQuery = "INSERT INTO \"OrderItems\" (orderid, itemid, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement itemsStmt = connection.prepareStatement(itemsQuery)) {
                for (Item item : cartItems) {
                    itemsStmt.setInt(1, orderid);
                    itemsStmt.setInt(2, item.getItemID());
                    itemsStmt.setInt(3, item.getItemQuantity());
                    itemsStmt.addBatch();
                }
                itemsStmt.executeBatch();
            }

            // Saving new items into table Inventory
            String inventoryQuery = "INSERT INTO \"Inventory\" (customerid, itemid, quantity) " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT (customerID, itemID) " +
                    "DO UPDATE SET quantity = \"Inventory\".quantity + EXCLUDED.quantity;";
            try (PreparedStatement inventoryStmt = connection.prepareStatement(inventoryQuery)) {
                for (Item orderItem : cartItems) {
                    inventoryStmt.setInt(1, customerID);
                    inventoryStmt.setInt(2, orderItem.getItemID());
                    inventoryStmt.setInt(3, orderItem.getItemQuantity());
                    inventoryStmt.addBatch();
                }
                inventoryStmt.executeBatch();
            }
            connection.commit(); // if everything went fine, we commit...
            return orderid;
        } catch (SQLException e) {
            try {
                connection.rollback(); // ...or we make a rollback
            } catch (SQLException e1) {
                System.out.println("Error while doing rollback: " + e1.getMessage());
            }
            throw new OrderExceptions.OrderSaveException("There was an error in managing the database: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e2) {
                System.out.println("Error while reactivating auto commit: " + e2.getMessage());
            }
        }
    }

    public void deleteOrder(int orderID) {
        String deletsql = "DELETE FROM \"Order\" WHERE orderid = ?";
        //TODO is this necessary? should we implement this function?
    }
}
