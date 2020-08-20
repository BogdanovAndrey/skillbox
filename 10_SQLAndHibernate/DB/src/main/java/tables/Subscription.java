package tables;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@NamedQueries({
        @NamedQuery(
                name = "getAllSubscriptions",
                query = "from Subscription"
        )})

@Data
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @EmbeddedId
    private SubscriptionID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("student")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("course")
    private Course course;

    @Column(name = "subscription_date")
    private LocalDateTime subscriptionDate;

    public Subscription() {
    }

    public Subscription(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.id = new SubscriptionID(student.getId(), course.getId());
    }

    public String toString() {
        return "********\n" +
                "Дата подписки: " +
                (DateTimeFormatter.ISO_DATE.format(subscriptionDate)) +
                "\nСтудент: " +
                student.getName() +
                "\nКурс: " +
                course.getName();
    }

    @Embeddable
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class SubscriptionID implements Serializable {
        @Column(name = "student_id")
        private int student;
        @Column(name = "course_id")
        private int course;

        public SubscriptionID() {
        }

        public SubscriptionID(int studentId, int courseId) {
            this.student = studentId;
            this.course = courseId;
        }
    }
}
