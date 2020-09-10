package dao;

import dao.util.AbstractDAO;
import org.hibernate.Session;
import tables.Student;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
        @NamedQuery(
                name = "getAllStudents",
                query = "from Student"
        )})

public class StudentDAO extends AbstractDAO<Student> {
    public StudentDAO(Session session) {
        super(session);
    }

    @Override
    public Student findByField(String field, String val) {
        return getSession().createQuery("Select s from Student s where s." + field + " = '" + val + "'", Student.class)
                .getSingleResult();
    }
}
