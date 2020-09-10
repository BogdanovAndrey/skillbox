package tables;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NamedQueries({
        @NamedQuery(
                name = "getAllPurchases",
                query = "from Purchase"
        )})

@Data
@Entity
@Table(name = "purchaselist")
public class Purchase {
    @EmbeddedId
    private PurchaseID id;

    @Column(name = "subscription_date")
    private LocalDateTime subscriptionDate;

    private Integer price;


    public String toString() {
        return "********\n" +
                "Дата подписки: " +
                (DateTimeFormatter.ISO_DATE.format(getSubscriptionDate())) +
                "\nСтудент: " +
                id.getStudentName() +
                "\nКурс: " +
                id.getCourseName() +
                "\nЦена: " +
                price;
    }

    @Embeddable
    @Data
    public static class PurchaseID implements Serializable {
        @Column(name = "student_name")
        private String studentName;
        @Column(name = "course_name")
        private String courseName;
    }
}
