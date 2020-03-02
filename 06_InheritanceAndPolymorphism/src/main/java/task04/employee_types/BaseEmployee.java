package task04.employee_types;

import task04.company.Company;

abstract public class BaseEmployee implements Employee, Comparable<BaseEmployee> {
    double salary;
    Company company;
    private String name;

    BaseEmployee(Company company, String name, double baseSalary) {
        this.company = company;
        this.name = name;
        salary = baseSalary;
    }

    public String getName() {
        return name;
    }

    @Override
    public double getMonthSalary() {
        return salary;
    }

    @Override
    public int compareTo(BaseEmployee o) {
        return Double.compare(o.getMonthSalary(), this.getMonthSalary());
    }
}
