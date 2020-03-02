package task04.company;

import task04.employee_types.BaseEmployee;
import task04.employee_types.Manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Company {
    final int MAX_INCOME = 100000000;
    private ArrayList<BaseEmployee> stuff = new ArrayList<>();

    public Company() {

    }

    public void hire(BaseEmployee newbie) {
        stuff.add(newbie);
        Collections.sort(stuff);
    }

    public void hireAll(List<BaseEmployee> newbieList) {
        stuff.addAll(newbieList);
        Collections.sort(stuff);
    }

    public boolean fire(BaseEmployee emp) {
        return stuff.remove(emp);
    }

    public int getIncome() {
        return new Random().nextInt(MAX_INCOME);
    }

    public List<BaseEmployee> getTopSalaryStaff(int count) {
        if (count > stuff.size()) {
            System.out.println("В компании меньше сотрудников, чем Вы запросили. " +
                    "Вывод ограничен актуальным числом сотрудников");
            count = stuff.size();
        }

        return stuff.subList(0, count);
    }

    public List<BaseEmployee> getLowestSalaryStaff(int count) {
        Collections.sort(stuff);
        if (count > stuff.size()) {
            System.out.println("В компании меньше сотрудников, чем Вы запросили. " +
                    "Вывод ограничен актуальным числом сотрудников");
            count = stuff.size();
        }
        return stuff.subList(stuff.size() - count, stuff.size());
    }

    public List<BaseEmployee> getStuff() {
        return stuff;
    }

    public int getManagerCount() {
        int managerCount = 0;
        for (BaseEmployee emp : stuff
        ) {
            if (emp.getClass().isInstance(new Manager(null, null, 0))) {
                managerCount++;
            }
        }
        return managerCount;
    }
}
