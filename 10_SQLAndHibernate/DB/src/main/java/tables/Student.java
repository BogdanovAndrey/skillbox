package tables;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
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


//    @ManyToMany(cascade = CascadeType.ALL,
//            mappedBy = "students",
//            fetch = FetchType.EAGER)
    @OneToMany(
            mappedBy = "student",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<Subscription> courses;

    @Override
    public String toString() {
        return "Имя: " + this.name + "\t Возраст: " + this.age + "\n";
    }
}
