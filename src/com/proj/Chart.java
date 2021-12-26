package com.proj;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;

public class Chart {

    public static DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    public static void CreateChart() {
        // write your code here
        JFreeChart chart = ChartFactory.createBarChart(
                "Общий объем финансирования по годам завершения строительства",
                "Год завершения",
                "Финансирование",
                dataset);
        CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
        NumberAxis numberAxisAxis = (NumberAxis) categoryPlot.getRangeAxis();
        numberAxisAxis.setNumberFormatOverride(NumberFormat.getIntegerInstance());
        chart.setBackgroundPaint(Color.YELLOW);
        chart.setPadding(new RectangleInsets(8, 8, 8, 8));
        try
        {
            Path path = Paths.get("src\\chartJava.jpeg");
            ChartUtilities.saveChartAsJPEG(new File(path.toString()), chart, 1920, 1080);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}