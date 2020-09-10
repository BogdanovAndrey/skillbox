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

    //@ManyToOne(fetch = FetchType.LAZY)
    // @MapsId("student")
    //private Student student;

    // @ManyToOne(fetch = FetchType.LAZY)
    //= @MapsId("course")
    //private Course course;

    @Column(name = "subscription_date")
    private LocalDateTime subscriptionDate;

    public Subscription() {
    }


    public String toString() {
        return "********\n" +
                "Дата подписки: " +
                (DateTimeFormatter.ISO_DATE.format(subscriptionDate)) +
                "\nСтудент: " +
                id.student.getName() +
                "\nКурс: " +
                id.course.getName();
    }

    @Embeddable
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class SubscriptionID implements Serializable {
        @OneToOne
        @JoinColumn(name = "student_id")
        private Student student;
        @OneToOne
        @JoinColumn(name = "course_id")
        private Course course;

    }
}
