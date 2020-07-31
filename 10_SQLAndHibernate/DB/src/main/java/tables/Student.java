package tables;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private Integer age;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "student",
            fetch = FetchType.EAGER)
    private List<Subscription> subscriptions;

    @ManyToMany(cascade = CascadeType.ALL,
            mappedBy = "students",
            fetch = FetchType.EAGER)
    private List<Course> courses;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
}
