package tables;

import javax.persistence.*;
import java.util.List;

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

    @ManyToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinTable(name = "Subscriptions",
            joinColumns = {@JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "student_id")})
    private List<Student> students;

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setPricePerHour(Float pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public void setStudentsCount(Integer studentsCount) {
        this.studentsCount = studentsCount;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public CourseType getType() {
        return type;
    }

    public void setType(CourseType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getStudentsCount() {
        return studentsCount;
    }

    public void setStudentsCount(int studentsCount) {
        this.studentsCount = studentsCount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(float pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
}
