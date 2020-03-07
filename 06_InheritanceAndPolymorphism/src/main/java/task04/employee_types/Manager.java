package task04.employee_types;

import task04.company.Company;

import java.util.Random;

public class Manager implements Employee {
    final static private double BONUS = 0.05;
    final static private int MAX_POTENTIAL = 1000;
    int potential;
    double salary;
    Company company;
    String name;

    public Manager(Company company, String name, double baseSalary) {
        this.salary = baseSalary;
        this.company = company;
        this.name = name;
        changePotential();
    }

    @Override
    public String getName() {
        return name;
    }

    public void changePotential() {
        potential = new Random().nextInt(MAX_POTENTIAL);
    }

    public double getSalesAmount() {
        //Объем продаж пропорционален зарплате, но не больше 100
        return salary * potential;
    }

    @Override
    public double getMonthSalary() {
        return salary + BONUS * getSalesAmount();
    }

}
