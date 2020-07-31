package tables;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Subscriptions")
public class Subscription implements Serializable {
    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    private Student student;

    @ManyToOne(cascade = CascadeType.ALL)
    private Course course;

    @Column(name = "subscription_date")
    private LocalDateTime subscriptionDate;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDateTime getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(LocalDateTime subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }
}
