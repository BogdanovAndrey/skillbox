package task04.company;

import task04.employee_types.Employee;
import task04.employee_types.Manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Company {
    final int MAX_INCOME = 100000000;
    private double monthIncome;
    private ArrayList<Employee> stuff = new ArrayList<>();

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

    public void countMonthIncome() {
        Manager man = new Manager(this, null, 0);
        monthIncome = 0;
        for (Employee emp : stuff
        ) {
            if (emp.getClass().isInstance(man)) {
                monthIncome += ((Manager) emp).getSalesAmount();
                ((Manager) emp).changePotential();
            }
        }
    }

    public double getMonthIncome() {
        return monthIncome;
    }

    public List<Employee> getTopSalaryStaff(int count) {
        return makeSalaryReport(stuff, 0, count);
    }

    public List<Employee> getLowestSalaryStaff(int count) {
        return makeSalaryReport(stuff, stuff.size() - count, stuff.size());
    }

    //Подготовка отчета по зарплатам
    private List<Employee> makeSalaryReport(List<Employee> stuff, int start, int end) {
        Collections.sort(stuff);
        if (start > end || start < 0 || end > stuff.size()) {
            System.out.println("В компании меньше сотрудников, чем Вы запросили. " +
                    "Вывод ограничен актуальным числом сотрудников");
            start = 0;
            end = stuff.size();
        }
        return stuff.subList(start, end);
    }

    public List<Employee> getStuff() {
        return stuff;
    }
}
