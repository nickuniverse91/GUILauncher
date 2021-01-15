package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// import org.sqlite.JDBC;

public class DBConnectionProvider {

    private String DB_PATH = "data\\";

    public Connection getConnection(String databaseName) {

        // The target format for the connection string is:
        // jdbc:sqlite:path\\database
        // jdbc:sqlite:D:\JavaProjects\SQLLiteSample\data\hr.db

        String engine = "jdbc:sqlite";

        String connectionString = engine + ":" + DB_PATH + databaseName;

        try {
            Connection dbConnection = DriverManager.getConnection(connectionString);

            return dbConnection;

        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }

    }

}
