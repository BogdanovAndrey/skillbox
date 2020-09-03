package tables;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
/*@Table(name = "linked_purchase_list",
        uniqueConstraints = { @UniqueConstraint(name = "uk_course_student_ids",
                columnNames = {"student_id", "course_id"})})
*/
@Data
public class LinkedPurchaseList {
    @EmbeddedId
    @NaturalId
    private LinkedPurchaseListID id;
    @Column(name = "student_id", insertable = false, updatable = false, unique = false)
    private Integer student;
    @Column(name = "course_id", insertable = false, updatable = false, unique = false)
    private Integer course;
    @Column(name = "course_name")
    private String courseName;
    @Column(name = "student_name")
    private String studentName;

    private Integer price;

    @Column(name = "subscription_date")
    private LocalDateTime subscriptionDate;


    @Embeddable
    @Data
    public static class LinkedPurchaseListID implements Serializable {
        @OneToOne
        @MapsId
        private Student student;

        @OneToOne
        @MapsId
        private Course course;

    }
}
