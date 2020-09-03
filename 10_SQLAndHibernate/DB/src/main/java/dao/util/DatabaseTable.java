package dao.util;

import java.util.List;

public interface DatabaseTable<L> {

    List<L> findAll();

    L findByField(String field, String val);

    void save(L input);

    void update(String statement, L input);
}
