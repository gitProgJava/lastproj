Проект по JAVA, Спортивные учреждения
==============
***Содержание:***
- [Разработка классов](#ClassMaker)
- [Парсер полей](#Parser)
- [Подключение БД SQLlite](#BD-connect)
- [Создание и сохранение данных в БД](#SaveBD)
- [Создание SQL запросов](#Travis-CI)
- [Вывод полученных данных](#Setup)
- [График по заданию 1](#Configuration)


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
