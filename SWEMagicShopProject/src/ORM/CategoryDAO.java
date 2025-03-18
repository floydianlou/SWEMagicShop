package ORM;
import DomainModel.*;

import java.sql.*;
import java.util.ArrayList;

public class CategoryDAO {
    private Connection connection;

    public CategoryDAO(){
        try{
            this.connection = ConnectionManager.getInstance().getConnection();;
        }
        catch(SQLException e){
            System.out.println("Something happened while trying to connect to database..." + e.getMessage());
        }
    }

    public ArrayList<String> viewAllCategories() {
        ArrayList<String> categories = new ArrayList<>();
        String query = "SELECT name FROM \"Category\"";

        try (PreparedStatement stmt = connection.prepareStatement(query)){
             try(ResultSet set = stmt.executeQuery()) {
                 while (set.next()){
                     String category = set.getString("name");
                     categories.add(category);
                 }
             }
        } catch (SQLException e) {
            System.out.println("Something happened while retrieving the categories: " + e.getMessage());
        }
        return categories;
    }
}