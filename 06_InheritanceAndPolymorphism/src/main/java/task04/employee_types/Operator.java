package task04.employee_types;

import task04.company.Company;

public class Operator implements Employee {

    double salary;
    Company company;
    String name;

    public Operator(Company company, String name, double baseSalary) {
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
        return salary;
    }
}
