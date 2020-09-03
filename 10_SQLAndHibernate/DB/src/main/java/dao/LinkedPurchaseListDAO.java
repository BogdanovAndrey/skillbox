package dao;

import dao.util.AbstractDAO;
import tables.LinkedPurchaseList;

import java.util.List;

public class LinkedPurchaseListDAO extends AbstractDAO<LinkedPurchaseList> {
    @Override
    public List<LinkedPurchaseList> findAll() {
        return (List<LinkedPurchaseList>) getSession().createQuery("from LinkedPurchaseList").list();
    }

    @Override
    public LinkedPurchaseList findByField(String field, String val) {
        return null;
    }

    @Override
    public void save(LinkedPurchaseList input) {

    }

    @Override
    public void update(String statement, LinkedPurchaseList input) {

    }

    public void fillTable() {
        getSession().createQuery(
                "insert into LinkedPurchaseList(student,course,courseName,studentName,price, subscriptionDate) " +
                        //  "insert into LinkedPurchaseList(id,courseName,studentName,price, subscriptionDate) " +
                        "select s.id, c.id, pl.id.courseName, pl.id.studentName, pl.price, pl.subscriptionDate " +
                        "from Purchase pl " +
                        "join Course c on c.name = pl.id.courseName " +
                        "join Student s on s.name = pl.id.studentName")
                .executeUpdate();
    }

    public void deleteAll() {
        getSession().createQuery(" delete from LinkedPurchaseList").executeUpdate();
    }
}
