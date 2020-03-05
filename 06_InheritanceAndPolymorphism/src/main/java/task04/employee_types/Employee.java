package task04.employee_types;

public interface Employee extends Comparable<Employee> {
    double getMonthSalary();

    String getName();

    @Override
    default int compareTo(Employee o) {
        return Double.compare(o.getMonthSalary(), this.getMonthSalary());
    }
}
