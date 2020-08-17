package tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "subscriptions")
public class Subscription {
    //        @Id
//    @ManyToOne(cascade = CascadeType.ALL)
//    private Student student;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    private Course course;
    @EmbeddedId
    private SubscriptionID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("student")
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("course")
    private Course course;

    @Column(name = "subscription_date")
    private LocalDateTime subscriptionDate;

    private Subscription() {
    }

    public Subscription(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.id = new SubscriptionID(student.getId(), course.getId());
    }

}
