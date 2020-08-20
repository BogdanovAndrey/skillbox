import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import tables.*;

import java.util.List;
import java.util.Scanner;

public class SQLer {

    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();

        // List <Purchase> l = session.getNamedQuery("getAllPurchases").list();

        Scanner input = new Scanner(System.in);
        try {
            System.out.println("\nВыберите таблицу для выдачи информации:");
            System.out.println("1 - Таблица Courses");
            System.out.println("2 - Таблица Students");
            System.out.println("3 - Таблица Teachers");
            System.out.println("4 - Таблица Subscriptions");
            System.out.println("5 - Таблица Purchases");
            System.out.print(">>  ");
            List result = getResult(session, input.nextInt());
            result.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Ошибка ввода");
        }


        session.close();

    }

    private static List getResult(Session session, int choice) {
        switch (choice) {
            case 1: {
                return (List<Course>) session.getNamedQuery("getAllCourses").list();
            }
            case 2: {
                return (List<Student>) session.getNamedQuery("getAllStudents").list();
            }
            case 3: {
                return (List<Teacher>) session.getNamedQuery("getAllTeachers").list();
            }
            case 4: {
                return (List<Subscription>) session.getNamedQuery("getAllSubscriptions").list();
            }
            case 5: {
                return (List<Purchase>) session.getNamedQuery("getAllPurchases").list();
            }
        }
        return null;
    }
}

