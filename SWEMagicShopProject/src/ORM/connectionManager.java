package ORM;

import java.sql.Connection;

public class connectionManager {
    private static final String url = "jdbc:postgresql://localhost:5432/otariMagicShop";
    private static final String username = "postgres";
    private static final String password = "postgres";
    private static Connection connection = null;

}
