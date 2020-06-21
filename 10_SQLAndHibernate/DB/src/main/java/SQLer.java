import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import tables.Course;

import java.util.List;
import java.util.Scanner;

public class SQLer {

    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();


        Session session = sessionFactory.openSession();

        Query<Course> query = session.createQuery("FROM tables.Course", Course.class);

        List<Course> list = query.list();

        session.close();

        Scanner input = new Scanner(System.in);
        try {
            System.out.println("\nВведите номер курса для выдачи информации:");
            System.out.println("(от 1 до " + list.size() + ")");
            System.out.print(">>  ");
            Course course = list.get(input.nextInt() - 1);
            System.out.println("\tНазвание курса: " + course.getName());
            System.out.println("\tКоличество студентов: " + course.getStudentsCount());
        } catch (Exception e) {
            System.out.println("Ошибка ввода");
        }



    }


}
