package task04;

import task04.employee_types.Employee;

import java.util.ArrayList;
import java.util.List;

public class Company {
    private ArrayList<Employee> stuff;

    public Company() {

    }

    public void hire(Employee newbie) {
        stuff.add(newbie);
    }

    public void hireAll(List<Employee> newbieList) {
        stuff.addAll(newbieList);
    }

    public boolean fire(Employee emp) {
        return stuff.remove(emp);
    }

    public double getIncome() {
        return 0;
    }

    public List<Employee> getTopSalaryStaff(int count) {
        return stuff;
    }

    public List<Employee> getLowestSalaryStaff(int count) {
        return stuff;
    }

    public List<Employee> getStuff() {
        return stuff;
    }
}
