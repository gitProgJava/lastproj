package com.proj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class sqlConnect {
    public static Connection connection;
    public static Statement statement;

    public static void ConnectDB() throws ClassNotFoundException, SQLException {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:data.sqlite");
            if (connection != null) {
                System.out.println("Есть контакт!");
            }
        } catch (Exception ex) {

        }
    }
    public static void CreateDB() throws SQLException {
        statement = connection.createStatement();

        statement.execute("CREATE TABLE if not exists 'SportInstitutesTable' ( 'Id' INT  PRIMARY KEY," +
                "'Name' STRING , 'startOfConstruction' DATE, 'endOfConstruction' DATE, 'totalMoney' LONG );");

    }
    public static void FillTable(String queryValue) throws SQLException {
        String query = "INSERT INTO 'SportInstitutesTable' ('Id', 'Name', 'startOfConstruction', 'endOfConstruction', 'totalMoney')";
        query += String.format("VALUES (%s)", queryValue);
        statement.execute(query);
    }
}