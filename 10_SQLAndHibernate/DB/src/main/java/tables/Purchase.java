package tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "purchaselist")
public class Purchase {

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "course_name")
    private String courseName;

    private Integer price;

    @Column(name = "subscription_date")
    private LocalDate subscriptionDate;
}
