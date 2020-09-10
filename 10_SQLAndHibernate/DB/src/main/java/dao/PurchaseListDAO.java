package dao;

import dao.util.AbstractDAO;
import org.hibernate.Session;
import tables.Purchase;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;


@NamedQueries({
        @NamedQuery(
                name = "getAllPurchases",
                query = "from Purchase"
        )})

public class PurchaseListDAO extends AbstractDAO<Purchase> {

    public PurchaseListDAO(Session session) {
        super(session);
    }

    @Override
    public List<Purchase> findAll() {
        return getSession().createNamedQuery("getAllPurchases", Purchase.class).list();
    }

    @Override
    public Purchase findByField(String field, String val) {
        return super.findByField(field, val);
    }

    @Override
    public void save(Purchase input) {
        super.save(input);
    }

    @Override
    public void update(String statement, Purchase input) {
        super.update(statement, input);
    }
}
