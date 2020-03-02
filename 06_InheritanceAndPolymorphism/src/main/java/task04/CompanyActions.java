package task04;

import task04.company.Company;
import task04.employee_types.BaseEmployee;
import task04.employee_types.Manager;
import task04.employee_types.Operator;
import task04.employee_types.TopManager;

import java.util.ArrayList;
import java.util.Random;

public class CompanyActions {
    final static int OPERATOR_COUNT = 180;
    final static int MANAGER_COUNT = 80;
    final static int TOP_MANAGER_COUNT = 10;
    final static int OPERATOR_DELTA_SALARY = 80000;
    final static int MANAGER_DELTA_SALARY = 180000;
    final static int TOP_MANAGER_DELTA_SALARY = 280000;
    final static int MIN_SALARY = 20000;

    public static void main(String[] args) {
        Company firstCompany = new Company();
        ArrayList<BaseEmployee> employees = new ArrayList<>();
        for (int i = 0; i < OPERATOR_COUNT; i++) {
            employees.add(new Operator(firstCompany, "Operator " + i, getSalary(OPERATOR_DELTA_SALARY)));
        }
        for (int i = 0; i < MANAGER_COUNT; i++) {
            employees.add(new Manager(firstCompany, "Manager " + i, getSalary(MANAGER_DELTA_SALARY)));
        }
        firstCompany.hireAll(employees);

        for (int i = 0; i < TOP_MANAGER_COUNT; i++) {
            firstCompany.hire(new TopManager(firstCompany, "Top Manager " + i, getSalary(TOP_MANAGER_DELTA_SALARY)));
        }

        System.out.println("Топ 5 зарплат в комании");
        for (BaseEmployee emp : firstCompany.getTopSalaryStaff(5)
        ) {
            System.out.println(emp.getName() + ": " + (int) emp.getMonthSalary());
        }

        System.out.println("\n5 минимальных зарплат в комании");
        for (BaseEmployee emp : firstCompany.getLowestSalaryStaff(5)
        ) {
            System.out.println(emp.getName() + ": " + (int) emp.getMonthSalary());
        }

    }

    private static int getSalary(int deltaSalary) {
        return (MIN_SALARY + (int) (new Random().nextDouble() * deltaSalary));
    }
}
