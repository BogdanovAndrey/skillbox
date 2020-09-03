package dao.util;

import lombok.Data;
import org.hibernate.Session;

import java.util.List;

@Data
public class AbstractDAO<L> implements DatabaseTable<L> {
    private Session session;

    @Override
    public List<L> findAll() {
        return null;
    }

    @Override
    public L findByField(String field, String val) {
        return null;
    }

    @Override
    public void save(L input) {

    }

    @Override
    public void update(String statement, L input) {

    }
}
