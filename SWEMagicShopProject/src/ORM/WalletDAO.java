package ORM;
import DomainModel.*;

import java.sql.*;


public class WalletDAO {

    private Connection connection;

    public WalletDAO() {
        try{
            this.connection = ConnectionManager.getInstance().getConnection();;
        }
        catch(SQLException e){
            System.out.println("Something happened while trying to connect to database..." + e.getMessage());
        }
    }

    public Wallet getWalletByID(int ID) throws RuntimeException{
        Wallet wallet = null;
        String query = "SELECT * FROM \"Wallet\" WHERE customerID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int walletID = resultSet.getInt("customerID");
                    int balanceCP = resultSet.getInt("CPbalance");
                    wallet = new Wallet(walletID, balanceCP);
                }else {
                    throw new RuntimeException("Customer ID not found: " + ID);
                }
            }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving your wallet: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
        return wallet;
    }

    public void updateWallet(Wallet wallet) throws RuntimeException {
        String query = "UPDATE \"Wallet\" SET CPbalance = ? WHERE customerID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, wallet.getCPbalance());
            stmt.setInt(2, wallet.getWalletID());
            int rowsAffected = stmt.executeUpdate();
            if( rowsAffected > 0 ){
                System.out.println("Wallet updated");
            } else {
                throw new RuntimeException("Customer ID not found: " + wallet.getWalletID());
            }
        } catch (SQLException e) {
            System.err.println("Something happened while updating your wallet: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
        }
    }
}