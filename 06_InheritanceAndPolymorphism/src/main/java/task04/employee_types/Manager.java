package task04.employee_types;

import task04.company.Company;

public class Manager extends BaseEmployee {
    final private double BONUS = 0.05;

    public Manager(Company company, String name, double baseSalary) {
        super(company, name, baseSalary);
    }

    @Override
    public double getMonthSalary() {
        return salary + (company.getIncome() * BONUS / company.getManagerCount());
    }
}
