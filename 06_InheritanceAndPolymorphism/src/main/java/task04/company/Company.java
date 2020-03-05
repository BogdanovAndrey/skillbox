package task04.company;

import task04.employee_types.Employee;
import task04.employee_types.Manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Company {
    final int MAX_INCOME = 100000000;
    private ArrayList<Employee> stuff = new ArrayList<>();

    public Company() {

    }

    public void hire(Employee newbie) {
        stuff.add(newbie);
        Collections.sort(stuff);
    }

    public void hireAll(List<Employee> newbieList) {
        stuff.addAll(newbieList);
        Collections.sort(stuff);
    }

    public boolean fire(Employee emp) {
        return stuff.remove(emp);
    }

    public int getIncome() {
        return 0;
    }

    public List<Employee> getTopSalaryStaff(int count) {
        if (count > stuff.size()) {
            System.out.println("В компании меньше сотрудников, чем Вы запросили. " +
                    "Вывод ограничен актуальным числом сотрудников");
            count = stuff.size();
        }

        return stuff.subList(0, count);
    }

    public List<Employee> getLowestSalaryStaff(int count) {
        Collections.sort(stuff);
        if (count > stuff.size()) {
            System.out.println("В компании меньше сотрудников, чем Вы запросили. " +
                    "Вывод ограничен актуальным числом сотрудников");
            count = stuff.size();
        }
        return stuff.subList(stuff.size() - count, stuff.size());
    }

    public List<Employee> getStuff() {
        return stuff;
    }

    public int getManagerCount() {
        int managerCount = 0;
        for (Employee emp : stuff
        ) {
            if (emp.getClass().isInstance(new Manager(null, null, 0))) {
                managerCount++;
            }
        }
        return managerCount;
    }
}
