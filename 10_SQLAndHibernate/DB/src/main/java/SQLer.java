import dao.SkillboxDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import tables.Course;
import tables.LinkedPurchaseList;
import tables.Purchase;
import tables.Student;

import java.util.List;

public class SQLer {

    public static void main(String[] args) {

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        //Подготовка сессии и транзакции для заполнения таблицы
        Session session = sessionFactory.openSession();
        Transaction tr = session.beginTransaction();

        //Экземпляр класса для работы со схемой skillbox
        SkillboxDAO skillboxDAO = new SkillboxDAO(session);

        //Очищаем таблицу для чистоты эксперимента
        skillboxDAO.getLinkedPurchaseListDAO().deleteAll();
    /*
        //Наполняем таблицу с помощью HQL запроса
        skillboxDAO.getLinkedPurchaseListDAO().fillTable();
        System.out.println("Rows added by HQL: " + skillboxDAO.getLinkedPurchaseListDAO().findAll().size());
        //Закрываем старую сессию и открываем новую (для повторного заполнения)
        session.close();
        session = sessionFactory.openSession();
        tr = session.beginTransaction();

        //Обновим сессию в классах
        skillboxDAO.setSession(session);
        //Очищаем таблицу для чистоты эксперимента
        skillboxDAO.getLinkedPurchaseListDAO().deleteAll();
    */
        //Наполним таблицу в цикле через создание экземпляров класса
        fillTableInCycle(skillboxDAO);
        System.out.println("Rows added in cycle: " + skillboxDAO.getLinkedPurchaseListDAO().findAll().size());

        session.close();

    }

    static void fillTableInCycle(SkillboxDAO skillboxDAO) {
        //Получим список из таблицы purchase_list
        List<Purchase> purchases = skillboxDAO.getPurchaseListDAO().findAll();

        purchases.stream().map(purchase -> {
            LinkedPurchaseList linkedPurchase = new LinkedPurchaseList();
            //Создаем ID новой записи
            LinkedPurchaseList.LinkedPurchaseListID linkedPurchaseID = new LinkedPurchaseList.LinkedPurchaseListID();
            //Для этого выберем студента по имени
            Student st = skillboxDAO.getStudentDAO().findByField("name", purchase.getId().getStudentName());
            linkedPurchaseID.setStudent(st);
            //Выберем название курса по имени
            Course cr = skillboxDAO.getCourseDAO().findByField("name", purchase.getId().getCourseName());
            //Заполняем поля новой записи
            linkedPurchaseID.setCourse(cr);
            linkedPurchase.setId(linkedPurchaseID);
            linkedPurchase.setCourseName(cr.getName());
            linkedPurchase.setStudentName(st.getName());
            linkedPurchase.setPrice(purchase.getPrice());
            linkedPurchase.setSubscriptionDate(purchase.getSubscriptionDate());
            return linkedPurchase;
        }).forEach(skillboxDAO.getSession()::save);//Сохраняем записи
    }
}

