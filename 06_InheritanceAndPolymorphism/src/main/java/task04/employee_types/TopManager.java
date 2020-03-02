package task04.employee_types;

import task04.company.Company;

public class TopManager extends BaseEmployee {
    final private double BONUS = 1.50;
    final private int MINIMAL_INCOME = 10000000;

    public TopManager(Company company, String name, double baseSalary) {
        super(company, name, baseSalary);
    }

    @Override
    public double getMonthSalary() {
        return salary + ((company.getIncome() > MINIMAL_INCOME) ? salary * BONUS : 0);
    }
}
