package dao;

import dao.util.AbstractDAO;
import org.hibernate.Session;
import tables.Course;

public class CourseDAO extends AbstractDAO<Course> {
    public CourseDAO(Session session) {
        super(session);
    }

    @Override
    public Course findByField(String field, String val) {
        return getSession().createQuery("Select c from Course c where c." + field + " = '" + val + "'", Course.class)
                .getSingleResult();
    }
}
