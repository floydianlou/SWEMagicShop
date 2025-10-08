package DAO;

import DomainModel.ArcaneRequest;
import Exceptions.ArcaneExceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ArcaneDAO {
    Connection connection;

    public ArcaneDAO() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException e) {
            System.out.println("Something happened while trying to connect to database..." + e.getMessage());
        }
    }

    public ArrayList<ArcaneRequest> viewRequestsByCustomer(int customerID) {
        String arcanesql = "SELECT a.requestid, c.customerid, c.name as customername, r.name as status, a.requestDate " +
                "FROM \"ArcaneRequest\" a JOIN \"Customer\" c on a.customerid = c.customerid " +
                "JOIN \"RequestStatus\" r on a.statusid = r.statusid " +
                "WHERE a.customerid = ? " +
                "ORDER BY a.requestid DESC;";
        ArrayList<ArcaneRequest> arcaneRequests = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(arcanesql)) {
            stmt.setInt(1, customerID);
            try (ResultSet set = stmt.executeQuery()) {
                while (set.next()) {
                    int requestid = set.getInt("requestid");
                    int customerid = set.getInt("customerid");
                    String customername = set.getString("customername");
                    String status = set.getString ("status");
                    String date = set.getString ("requestDate");
                    arcaneRequests.add(new ArcaneRequest(requestid, status, customerid, customername, date));
                }
            }
        } catch (SQLException e) {
            throw new ArcaneExceptions.ArcaneViewException("Something happened while trying to GUI.view your data...");
        }
        return arcaneRequests;
    }

    public int countCustomerRequests(int customerID) throws ArcaneExceptions.ArcaneCountException {
        String countSql = "SELECT COUNT(*) FROM \"ArcaneRequest\" WHERE customerID = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(countSql)) {
            stmt.setInt(1, customerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new ArcaneExceptions.ArcaneCountException("Can't retrieve your data at the moment...");
        }
        throw new ArcaneExceptions.ArcaneCountException("Can't retrieve your data at the moment...");
    }

    public boolean canMakeArcaneRequest(int customerID) {
        String pendingSql = "SELECT (EXISTS (SELECT 1 FROM \"ArcaneRequest\" WHERE customerID = ? AND statusid = 1)) " +
                "OR ((SELECT COUNT(*) FROM \"ArcaneRequest\" WHERE customerID = ?) >= 5) AS result";

        try (PreparedStatement stmt = connection.prepareStatement(pendingSql)) {
            stmt.setInt(1, customerID);
            stmt.setInt(2, customerID);
            try (ResultSet res = stmt.executeQuery()){
                if (res.next()) {
                    return !res.getBoolean("result");
                }
            }
        } catch (SQLException e) {
            throw new ArcaneExceptions.ArcaneCreationException("Something happened while working on database...");
        }
        throw new ArcaneExceptions.ArcaneCreationException("Something went wrong while collecting your data.");
    }


    public int createArcaneRequest(int customerID) {
        String requestSql = "INSERT INTO \"ArcaneRequest\" (customerid) VALUES (?) RETURNING requestid;";
        try (PreparedStatement stmt = connection.prepareStatement(requestSql)) {
            stmt.setInt(1,customerID);
            try (ResultSet set = stmt.executeQuery()) {
                if (!set.next()) {
                    throw new SQLException("Failed to create new request: no ID returned.");
                }
                return set.getInt("requestid"); // returns request id to GUI.view in gui
            }
        } catch (SQLException e) {
            throw new ArcaneExceptions.ArcaneCreationException("Something went wrong while creating your request.");
        }
    }


    public ArrayList<ArcaneRequest> viewAllArcaneRequests() {
        String allsql = "SELECT a.requestid, c.customerid, c.name as customername, r.name as status, a.requestDate " +
                "FROM \"ArcaneRequest\" a JOIN \"Customer\" c on a.customerid = c.customerid " +
                "JOIN \"RequestStatus\" r on a.statusid = r.statusid;";
        ArrayList<ArcaneRequest> arcaneRequests = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(allsql)) {
            try (ResultSet set = stmt.executeQuery()) {
                while (set.next()) {
                    int requestid = set.getInt("requestid");
                    int customerid = set.getInt("customerid");
                    String customername = set.getString("customername");
                    String status = set.getString ("status");
                    String date = set.getString ("requestDate");
                    arcaneRequests.add(new ArcaneRequest(requestid, status, customerid, customername, date));
                }
            }
        } catch (SQLException e) {
            throw new ArcaneExceptions.ArcaneViewException("Something happened while trying to GUI.view your data...");
        }
        return arcaneRequests;
    }

    public ArrayList<ArcaneRequest> viewPendingRequests() {
        String pendingsql = "SELECT a.requestid, c.customerid, c.name as customername, r.name as status, a.requestDate " +
                "FROM \"ArcaneRequest\" a JOIN \"Customer\" c on a.customerid = c.customerid " +
                "JOIN \"RequestStatus\" r on a.statusid = r.statusid " +
                "WHERE a.statusid = 1;";
        ArrayList<ArcaneRequest> arcaneRequests = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(pendingsql)) {
            try (ResultSet set = stmt.executeQuery()) {
                while (set.next()) {
                    int requestid = set.getInt("requestid");
                    int customerid = set.getInt("customerid");
                    String customername = set.getString("customername");
                    String status = set.getString ("status");
                    String date = set.getString ("requestDate");
                    arcaneRequests.add(new ArcaneRequest(requestid, status, customerid, customername, date));
                }
            }
        } catch (SQLException e) {
            throw new ArcaneExceptions.ArcaneViewException("Something happened while trying to GUI.view your data...");
        }
        return arcaneRequests;
    }

    public boolean updateArcaneRequest(ArcaneRequest arcaneRequest, int updatedStatus) {
        String approvesql = "UPDATE \"ArcaneRequest\" SET statusid = ? WHERE requestid = ?";

        boolean usetransaction = (updatedStatus == 2);
        try {
            if (usetransaction) {
                connection.setAutoCommit(false); }
            try (PreparedStatement stmt = connection.prepareStatement(approvesql)) {
                stmt.setInt(1, updatedStatus);
                stmt.setInt(2, arcaneRequest.getRequestID());
                stmt.executeUpdate();

                if (usetransaction) {
                    String arcanesql = "UPDATE \"Customer\" SET arcanemembership = ? WHERE customerID = ?;";
                    try (PreparedStatement memberstmt = connection.prepareStatement(arcanesql)) {
                        memberstmt.setBoolean(1, true);
                        memberstmt.setInt(2, arcaneRequest.getCustomerID());
                        memberstmt.executeUpdate();
                    }
                }
            }
            if (usetransaction) { connection.commit(); }
            return true;
        } catch (SQLException e) {
            if (usetransaction) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    System.out.println("Error while doing rollback: " + e1.getMessage());
                }
            }
            throw new ArcaneExceptions.ArcaneCreationException("There was an error in managing the database: " + e.getMessage());
        } finally {
            if (usetransaction) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e2) {
                    System.out.println("Error while reactivating auto commit: " + e2.getMessage());
                }
            }
        }
    }
}