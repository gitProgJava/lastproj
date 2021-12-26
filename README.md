Проект по JAVA, Спортивные учреждения
==============
***Содержание:***
- [Разработка классов](#ClassMaker)
- [Парсер полей](#Parser)
- [Подключение БД SQLlite](#BD-connect)
- [Создание и сохранение данных в БД](#SaveBD)
- [Создание SQL запросов](#SqlQuery)
- [График по заданию 1](#Graph1)


# Разработка классов <a name="ClassMaker"></a>

Для хранения данных, получаемых из csv файла будем использовать класс SportInstitute, который хранит в себе Id, Name, startOfConstruction, endOfConstruction, totalMoney

```java
public class SportInstitute {
    String id;
    String Name;
    String startOfConstruction;
    String endOfConstruction;
    String totalMoney;
    public SportInstitute(String id, String name, String start, String end, String money) {
        this.id = id;
        this.Name = name;
        this.startOfConstruction = start;
        this.endOfConstruction = end;
        this.totalMoney = money;
    }
    @Override
    public String toString() {
        return String.format("'%s','%s','%s','%s','%s'",
                id,
                Name,
                startOfConstruction,
                endOfConstruction,
                totalMoney);
    }
```
# Парсер полей <a name="Parser"></a>

Для парсинга воспользуемся библеотекой OpenCSV, прогоним все строки по парсеру, удалим ненуженый хедер и лишние запятые. Промежуточные результаты выведем в консоль
```java
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
            }
        }
    }
}
```
Вывод полученных данных в консоль
![alt text](https://github.com/gitProgJava/lastproj/blob/main/src/4.PNG?raw=true)

Из - за того что в csv файле находится неполная информация, у некоторых записей нету дат и другой информации. Для того, чтобы не возникало проблем, установим для них дефолтную дату 1 января 1970 года.
```java
    private static String checkDates(String date) throws ParseException {
        if (date.isEmpty()) {
            var g = new SimpleDateFormat("dd.MM.yyyy").parse(String.format("01.01.%s", 1970)).getTime();
            return Long.toString(g);
        }
        return Long.toString(new SimpleDateFormat("dd.MM.yyyy").parse(date).getTime());
    }
```
## Подключение БД SQLlite <a name="BD-connect"></a>
Для подключения к SQLlite воспользуемся библеотекой sqllite-jdbc.
```java
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
```

## Создание и сохранение данных в БД <a name="SaveBD"></a>
Создаем БД и создаем столбцы с полями SportInstitute.
```java
    public static void CreateDB() throws SQLException {
        statement = connection.createStatement();

        statement.execute("CREATE TABLE if not exists 'SportInstitutesTable' ( 'Id' INT  PRIMARY KEY," +
                "'Name' STRING , 'startOfConstruction' DATE, 'endOfConstruction' DATE, 'totalMoney' LONG );");

    }
```
Заполняем данными из таблицы
```java
    public void fillTable() {
        for (SportInstitute sI : arr) {
            try {
                sqlConnect.FillTable(sI.toString());
            } catch (Exception e) {
            }
        }
    }
    public static void FillTable(String queryValue) throws SQLException {
        String query = "INSERT INTO 'SportInstitutesTable' ('Id', 'Name', 'startOfConstruction', 'endOfConstruction', 'totalMoney')";
        query += String.format("VALUES (%s)", queryValue);
        statement.execute(query);
    }
```
Дату хранил в милисекундах ради удобства вычислений в дальнейшем
![alt text](https://github.com/gitProgJava/lastproj/blob/main/src/5.PNG?raw=true)

## Создание SQL запросов <a name="SqlQuery"></a>
Для первого задания из полученного диапазона годов формирую массив, с помощью которого через preparedStatement выполняю SQL запрос попутно добавляю данные в dataset для оформления графика по полученным данным
```java
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
```
Пример полученных данных для задания 1
![alt text](https://github.com/gitProgJava/lastproj/blob/main/src/1.PNG?raw=true)

Для второго аналогично первому через preparedStatement выполняю запрос
```java
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
```
Пример полученных данных для задания 2
![alt text](https://github.com/gitProgJava/lastproj/blob/main/src/3.PNG?raw=true)

Для третьего задания через SQLзапрос вывожу постройку из числа стадионов и многофункциональных комплексов с максимальным финансированием
```java
    public static void getName() throws SQLException {
        //Task 3
        String averageCount = "SELECT Name, MAX(totalMoney) AS MONEY FROM SportInstitutesTable " +
                "WHERE Name LIKE 'Стадион%' or Name LIKE 'Многофункциональный спортивный комплекс%'";
        results = statement.executeQuery(averageCount);
        System.out.println("Максимальный объем  будет у "  + results.getString("Name") + " с результатом " + results.getString("MONEY"));
    }
```
Пример полученных данных для задания 3
![alt text](https://github.com/gitProgJava/lastproj/blob/main/src/2.PNG?raw=true)

## График к заданию 1 <a name="Graph1"></a>
Для построения графика использовал библеотеку JFreeChart. Создаю тайтлы по бокам, сверху и снизу. Заполняю данными из dataset'a, сохраняю в формате jpeg в FullHD.
```java
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
```
График к первому заданию
![alt text](https://github.com/gitProgJava/lastproj/blob/main/src/chartJava.jpeg?raw=true)
