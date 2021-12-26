package com.proj;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import java.sql.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Main {
    public static void main(String[] args) throws IOException, CsvException, SQLException, ClassNotFoundException, ParseException {
            sqlConnect.ConnectDB();
            sqlConnect.CreateDB();
            new OpenCSVParser("s.csv").fillTable();
            console23.getAvg();
            console23.getName();
            console23.getSumForYear(2006,2021);
        }
    }


