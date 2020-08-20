package tables;

import lombok.Data;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(
                name = "getAllTeachers",
                query = "from Teacher"
        ),
        @NamedQuery(
                name = "getTeachersByName",
                query = "select t from Teacher t where t.name like :name"
        )
})

@Data
@Entity
@Table(name = "Teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    private Integer salary;

    private Integer age;

    public String toString() {
        return "\nПреподаватель: " + name
                + "\n\t Возраст - " + age
                + "\n\t Зарплата - " + salary;
    }
}
