import dao.LinkedPurchaseListDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class SQLer {

    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();
        Transaction tr = session.beginTransaction();

        LinkedPurchaseListDAO lp = new LinkedPurchaseListDAO();
        lp.setSession(session);

        //lp.findAll().forEach(System.out::println);

        lp.deleteAll();

        lp.fillTable();
        lp.findAll().forEach(System.out::println);

        tr.commit();
        session.close();

    }

}

