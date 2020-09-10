package dao;

import lombok.Data;
import org.hibernate.Session;

@Data
public class SkillboxDAO {
    private Session session;
    private CourseDAO courseDAO;
    private LinkedPurchaseListDAO linkedPurchaseListDAO;
    private PurchaseListDAO purchaseListDAO;
    private StudentDAO studentDAO;


    public SkillboxDAO(Session session) {
        this.session = session;
        courseDAO = new CourseDAO(session);
        linkedPurchaseListDAO = new LinkedPurchaseListDAO(session);
        purchaseListDAO = new PurchaseListDAO(session);
        studentDAO = new StudentDAO(session);
    }

    public void setSession(Session session) {
        this.session = session;
        courseDAO.setSession(session);
        linkedPurchaseListDAO.setSession(session);
        purchaseListDAO.setSession(session);
        studentDAO.setSession(session);
    }
}
