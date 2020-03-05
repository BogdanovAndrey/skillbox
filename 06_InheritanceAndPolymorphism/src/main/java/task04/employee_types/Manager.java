package task04.employee_types;

import task04.company.Company;

import java.util.Random;

public class Manager implements Employee {
    final private double BONUS = 0.05;
    double salary;
    Company company;
    String name;

    public Manager(Company company, String name, double baseSalary) {
        this.salary = baseSalary;
        this.company = company;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public double getSalesAmount() {
        //Объем продаж пропорционален зарплате, но не больше 100
        return salary * (1 + new Random().nextInt(99));
    }

    @Override
    public double getMonthSalary() {
        return salary + BONUS * getSalesAmount();
    }

}
