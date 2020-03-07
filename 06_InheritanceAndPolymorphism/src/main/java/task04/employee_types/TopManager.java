package task04.employee_types;

import task04.company.Company;

public class TopManager implements Employee {
    final private double BONUS = 1.50;
    final private int MINIMAL_INCOME = 10000000;

    double salary;
    Company company;
    String name;

    public TopManager(Company company, String name, double baseSalary) {
        this.salary = baseSalary;
        this.company = company;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getMonthSalary() {
        return salary + ((company.getMonthIncome() > MINIMAL_INCOME) ? salary * BONUS : 0);
    }
}
