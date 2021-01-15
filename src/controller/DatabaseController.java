package controller;

import database.DBConnectionProvider;
import model.ProgramData;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseController {

    private static String databaseName = "ProgramDatabase.db";

    private static String tableName = "programs";

    private static ArrayList<ProgramData> dataList = new ArrayList<>();

    public static ArrayList<ProgramData> getTableData() throws SQLException {

        dataList.clear();

        DBConnectionProvider dbp = new DBConnectionProvider();

        Connection dbConnection = dbp.getConnection(databaseName);

        String query = "select * from " + tableName;

        Statement stmt = dbConnection.createStatement();

        ResultSet resultSet = stmt.executeQuery(query);

        while (resultSet.next()) {

            ProgramData dm = new ProgramData(resultSet.getString("name"), resultSet.getString("path"));

            dataList.add(dm);

        }

        return dataList;

    }

    public static void saveTableData(ArrayList<ProgramData> data) throws SQLException {

        DBConnectionProvider dbp = new DBConnectionProvider();

        Connection dbConnection = dbp.getConnection(databaseName);

        String query = "INSERT INTO " + tableName + " (name, path) VALUES (?,?)";


        PreparedStatement prep = dbConnection.prepareStatement(query);
        for (ProgramData d : data) {

            prep.setString(1, d.getName());
            prep.setString(2, d.getPath());

        }
        prep.executeUpdate();

    }

    public static void deleteEntry(String name) throws SQLException {

        DBConnectionProvider dbp = new DBConnectionProvider();

        Connection dbConnection = dbp.getConnection(databaseName);

        String query = "DELETE FROM " + tableName + " WHERE name='" + name + "'";


        PreparedStatement prep = dbConnection.prepareStatement(query);

        prep.executeUpdate();

    }

    public static void deleteAll() throws SQLException {

        DBConnectionProvider dbp = new DBConnectionProvider();

        Connection dbConnection = dbp.getConnection(databaseName);

        String query = "DELETE FROM " + tableName;


        PreparedStatement prep = dbConnection.prepareStatement(query);

        prep.executeUpdate();

    }
}
