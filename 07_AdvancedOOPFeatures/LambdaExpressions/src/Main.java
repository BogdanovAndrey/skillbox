import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    private static String staffFile = "data/staff.txt";
    private static String dateFormat = "dd.MM.yyyy";
    private final static int testYear = 2017;
    public static void main(String[] args) {
        ArrayList<Employee> staff = loadStaffFromFile();

//Задание №1
        staff.sort(Comparator.comparingInt(Employee::getSalary).thenComparing(Employee::getName));
        staff.forEach(System.out::println);
//Задание №2, часть 1
        System.out.println("\nМаксимальная зарплата сотрудников, принятых в 2017 году:");
        staff.stream().filter(e -> LocalDate.ofInstant(e.getWorkStart().toInstant(), ZoneId.systemDefault()). //Переводим Date в LocalDate
                getYear() == LocalDate.of(testYear, 1, 1).getYear()). //Выделяем тех, кто пришел в 2017 году
                max(Comparator.comparing(Employee::getSalary)).ifPresent(e -> System.out.println(e.getSalary())); //Находим максимальную зарплату

    }

    private static ArrayList<Employee> loadStaffFromFile() {
        ArrayList<Employee> staff = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(staffFile));
            for (String line : lines) {
                String[] fragments = line.split("\t");
                if (fragments.length != 3) {
                    System.out.println("Wrong line: " + line);
                    continue;
                }
                staff.add(new Employee(
                        fragments[0],
                        Integer.parseInt(fragments[1]),
                        (new SimpleDateFormat(dateFormat)).parse(fragments[2])
                ));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return staff;
    }

}