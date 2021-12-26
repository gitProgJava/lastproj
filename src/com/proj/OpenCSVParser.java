package com.proj;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OpenCSVParser {
    public ArrayList<SportInstitute> arr = new ArrayList<>();
    public OpenCSVParser(String path) throws IOException, CsvException {
        CSVParser csvParser = new CSVParserBuilder().withSeparator(',').build();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(path), "Cp1251"))
                .withCSVParser(csvParser)
                .withSkipLines(1)
                .build()) {
            {
                List<String[]> r = reader.readAll();
                r.forEach(x ->
                {
                    try {
                        arr.add(new SportInstitute(x[0], x[1], checkDates(x[18]), checkDates(x[19]),x[20]));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println(x[0] + " | " + x[1] + " | " + x[18] + " | " + x[19] + " | " + x[20]);
                });
                System.out.println(arr.get(0));
            }
        }
    }
    public void fillTable() {
        for (SportInstitute sI : arr) {
            try {
                sqlConnect.FillTable(sI.toString());
            } catch (Exception e) {
            }
        }
    }
    private static String checkDates(String date) throws ParseException {
        if (date.isEmpty()) {
            var g = new SimpleDateFormat("dd.MM.yyyy").parse(String.format("01.01.%s", 1900)).getTime();
            return Long.toString(g);
        }
        return Long.toString(new SimpleDateFormat("dd.MM.yyyy").parse(date).getTime());
    }
}

