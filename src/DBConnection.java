
package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBConnection {

    public static Connection getConnection() throws SQLException {

        Properties properties = new Properties();

        try (FileInputStream file =
                 new FileInputStream("config/db.properties")) {

            properties.load(file);

        } catch (IOException e) {
            throw new SQLException(
                "Unable to load database configuration. Check config/db.properties.",
                e
            );
        }

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        if (url == null || user == null || password == null) {
            throw new SQLException(
                "Database configuration is incomplete."
            );
        }

        Connection connection =
            DriverManager.getConnection(url, user, password);

        System.out.println("Database connected successfully!");

        return connection;
    }
}
