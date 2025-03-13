package ORM;
import java.sql.*;
import java.util.ArrayList;
import DomainModel.Item;
import DomainModel.Order;
import Exceptions.OrderExceptions;

public class OrderDAO implements DAOInterface.OrderDAO {

    private Connection connection;

    public OrderDAO() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException e) {
            System.out.println("Something happened while trying to connect to database..." + e.getMessage());
        }
    }

    @Override
    public ArrayList<Order> viewAllOrders() {
        // TODO check if it can be transformed into preparedstatement
        ArrayList<Order> orders = new ArrayList<>();
        String ordersql = String.format( "SELECT o.orderid, o.customerid, c.name, c.email, o.orderdate, s.name AS status_name, o.totalcp " +
                "FROM \"Order\" o " +
                "JOIN \"OrderStatus\" s ON o.statusid = s.statusid " +
                "JOIN \"Customer\" c ON o.customerid = c.customerid;");
        try (Statement stmt = connection.createStatement();
             ResultSet set = stmt.executeQuery(ordersql)){
            while (set.next()) {
                int orderid = set.getInt("orderid");
                int customerid = set.getInt("customerid");
                String orderdate = set.getString("orderdate");
                String status = set.getString("status_name");
                int totalcp = set.getInt("totalcp");
                Order newOrder = new Order(orderid, status, totalcp, orderdate, customerid);
                orders.add(newOrder);
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your orders: " + e.getMessage());
        }
        return orders;
    }

    @Override
    public ArrayList<Order> getCustomerOrders(int customerID) {
        // TODO transform into prepared statement
        ArrayList<Order> orders = new ArrayList<>();
        String ordersql = String.format("SELECT o.orderid, o.orderdate, s.name, o.totalcp " +
                "FROM \"Order\" o JOIN \"OrderStatus\" s ON o.statusid = s.statusid " +
                "WHERE o.customerID = %d;", customerID);
        try (Statement stmt = connection.createStatement();
             ResultSet set = stmt.executeQuery(ordersql)){
            while (set.next()) {
                int orderid = set.getInt("orderid");
                String orderdate = set.getString("orderdate");
                String status = set.getString("name");
                int totalcp = set.getInt("totalcp");
                Order newOrder = new Order(orderid, status, totalcp, orderdate);
                orders.add(newOrder);
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your orders: " + e.getMessage());
        }
        return orders;
    }

    @Override
    public int saveNewOrder(int orderTotal, int customerID, ArrayList<Item> cartItems) throws OrderExceptions.OrderSaveException {
        // TODO check if it works correctly
        String ordersql = "INSERT INTO \"Order\" (customerid, statusid, totalcp) VALUES (?, 1, ?) RETURNING orderid;";

        try (PreparedStatement statement = connection.prepareStatement(ordersql)) {
            connection.setAutoCommit(false);
            statement.setInt(1, customerID);
            statement.setInt(2, orderTotal);
            ResultSet set = statement.executeQuery();
            if (!set.next()) {
                throw new SQLException("Failed to save new order: no ID returned.");
            }
            int orderid = set.getInt("orderid");

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

            connection.commit();
            return orderid;

        } catch (SQLException e) {
            throw new OrderExceptions.OrderSaveException("There was an error in managing the database: " + e.getMessage());
        }
    }

    @Override
    public void deleteOrder(int orderID) {
        // TODO
    }
}
