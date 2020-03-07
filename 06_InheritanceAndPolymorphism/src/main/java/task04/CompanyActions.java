package task04;

import task04.company.Company;
import task04.employee_types.Employee;
import task04.employee_types.Manager;
import task04.employee_types.Operator;
import task04.employee_types.TopManager;

import java.util.ArrayList;
import java.util.Random;

public class CompanyActions {
    private final static int OPERATOR_COUNT = 180;
    private final static int MANAGER_COUNT = 80;
    private final static int TOP_MANAGER_COUNT = 10;
    private final static int OPERATOR_DELTA_SALARY = 40000;
    private final static int MANAGER_DELTA_SALARY = 80000;
    private final static int TOP_MANAGER_DELTA_SALARY = 2800000;
    private final static int MIN_SALARY = 20000;

    public static void main(String[] args) {
        //Настройка для работы сортировки списка сотрудников.
        // Без этого периодически вылетало исключение
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");

        //Создаем компанию
        Company firstCompany = new Company();

        createAndHireStuff(firstCompany);

        printNewMonthReport(firstCompany);

        decimateCompany(firstCompany);

        printNewMonthReport(firstCompany);
    }

    private static int getSalary(int deltaSalary) {
        return (MIN_SALARY + (int) (new Random().nextDouble() * deltaSalary));
    }

    private static void printTopSalary(Company company, int count) {
        System.out.println(count + " высоких зарплат в компании");
        for (Employee emp : company.getTopSalaryStaff(count)
        ) {
            System.out.println(emp.getName() + ": " + (int) emp.getMonthSalary());
        }
        System.out.println();

    }

    private static void printLowSalary(Company company, int count) {
        System.out.println(count + " низких зарплат в компании");
        for (Employee emp : company.getLowestSalaryStaff(count)
        ) {
            System.out.println(emp.getName() + ": " + (int) emp.getMonthSalary());
        }
        System.out.println();
    }

    private static void createAndHireStuff(Company company) {
        ArrayList<Employee> employees = new ArrayList<>();

        //Готовим список операторов для найма
        for (int i = 0; i < OPERATOR_COUNT; i++) {
            employees.add(new Operator(company, "Operator " + i, getSalary(OPERATOR_DELTA_SALARY)));
        }
        //Добавляем туда менеджеров
        for (int i = 0; i < MANAGER_COUNT; i++) {
            employees.add(new Manager(company, "Manager " + i, getSalary(MANAGER_DELTA_SALARY)));
        }
        //Нанимаем всех сразу
        company.hireAll(employees);

        //Нанимаем топ менеджеров напрямую
        for (int i = 0; i < TOP_MANAGER_COUNT; i++) {
            company.hire(new TopManager(company, "Top Manager " + i, getSalary(TOP_MANAGER_DELTA_SALARY)));
        }
    }

    private static void printNewMonthReport(Company company) {
        company.countMonthIncome();
        System.out.format("Компания заработала: %.2f руб.%n", company.getMonthIncome());

        printTopSalary(company, 15);
        printLowSalary(company, 30);
    }

    private static void decimateCompany(Company company) {
        //Уволим каждого второго сотрудника
        for (int i = 0; i < company.getStuff().size(); i++) {
            company.fire(company.getStuff().get(i + 1));
        }
    }
}
