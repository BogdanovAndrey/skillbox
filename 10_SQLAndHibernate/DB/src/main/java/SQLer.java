import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLer {
    private final static String URL = "jdbc:mysql://localhost:3306/skillbox?useUnicode=true&serverTimezone=UTC";
    private final static String PASS = "Testtest123";
    private final static String USER = "BogdAn";
    private final static String OUTPUT_FORMAT = "|%-40s|%-30s|%n";


    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASS);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT Courses.name AS Course_name, (COUNT(MONTH(subscription_date))/12) AS Average_sale_per_month \n" +
                    "FROM Subscriptions \n" +
                    "JOIN Courses ON Courses.id = Subscriptions.course_id\n" +
                    "group  by Courses.name;");

            printRow("Название курса", "Среднее кол-во продаж в месяц");

            while (rs.next()) {
                String name = rs.getString("Course_name");
                String avg = rs.getString("Average_sale_per_month");
                printRow(name, avg);

            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printRow(String col1, String col2) {
        System.out.printf(OUTPUT_FORMAT, col1, col2);

    }

}
