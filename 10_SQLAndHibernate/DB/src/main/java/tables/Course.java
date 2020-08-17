package tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;

    private Integer duration;

    private String name;

    private Integer price;

    @Column(name = "price_per_hour")
    private Float pricePerHour;

    @Column(name = "students_count")
    private Integer studentsCount;

    @ManyToOne(cascade = CascadeType.ALL)
    private Teacher teacher;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum")
    private CourseType type;

    //    @ManyToMany(cascade = CascadeType.ALL,
//            fetch = FetchType.EAGER)
//    @JoinTable(name = "Subscriptions",
//            joinColumns = {@JoinColumn(name = "course_id")},
//            inverseJoinColumns = {@JoinColumn(name = "student_id")})
    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<Subscription> students;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Курс: ").append(this.name);
        sb.append("\nОписание курса: ").append(this.description);
        sb.append("\nПродолжительность курса: ").append(this.duration);
        sb.append("\nСтоимость: ").append(this.price);
        sb.append("\nПродано: ").append(this.studentsCount);
        sb.append("\nОбучающиеся:\n");
        students.forEach(subscription -> sb.append(subscription.getStudent()));
        return sb.toString();
    }
}
