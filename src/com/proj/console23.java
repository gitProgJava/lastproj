package com.proj;

import org.jfree.data.category.DefaultCategoryDataset;

import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static com.proj.sqlConnect.connection;
import static com.proj.sqlConnect.statement;

public class console23 {
    public static ResultSet results;
    public static Statement statement;

    static {
        try {
            statement = sqlConnect.connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getSumForYear(int Startdate, int Enddate) throws SQLException, ParseException {
        for (var x = Startdate; x <= Enddate; x++)
        {
            String avgFor2012 = "SELECT SUM(totalMoney) FROM 'SportInstitutesTable' WHERE endOfConstruction >= ? AND endOfConstruction <= ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(avgFor2012))
            {
                preparedStatement.setObject(1, new SimpleDateFormat("dd.MM.yyyy").parse(String.format("01.01.%s", x)).getTime());
                preparedStatement.setObject(2, new SimpleDateFormat("dd.MM.yyyy").parse(String.format("31.12.%s", x)).getTime());
                results = preparedStatement.executeQuery();
                System.out.println("Объем финансирования по окончанию " + x + " года " + results.getLong(1));
                var dataset = new DefaultCategoryDataset();
                Chart.dataset.addValue(results.getDouble(1), "Jбъем финансирования", String.valueOf(x));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Chart.CreateChart();
    }
    public static void getAvg() throws SQLException, ParseException {
        //Task 2
        String avgFor2012 = "SELECT AVG(totalMoney) FROM 'SportInstitutesTable' WHERE startOfConstruction >= ? AND startOfConstruction <= ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(avgFor2012))
        {
            preparedStatement.setObject(1, new SimpleDateFormat("dd.MM.yyyy").parse(String.format("01.01.%s", 2012)).getTime());
            preparedStatement.setObject(2, new SimpleDateFormat("dd.MM.yyyy").parse(String.format("31.12.%s", 2012)).getTime());
            results = preparedStatement.executeQuery();
            System.out.println("Объем финансирования за 2012 год " + results.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getName() throws SQLException {
        //Task 3
        String averageCount = "SELECT Name, MAX(totalMoney) AS MONEY FROM SportInstitutesTable " +
                "WHERE Name LIKE 'Стадион%' or Name LIKE 'Многофункциональный спортивный комплекс%'";
        results = statement.executeQuery(averageCount);
        System.out.println("Максимальный объем  будет у "  + results.getString("Name") + " с результатом " + results.getString("MONEY"));
    }
}
