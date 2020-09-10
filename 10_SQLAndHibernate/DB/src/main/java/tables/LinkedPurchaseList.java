package tables;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
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
        @ManyToOne
        @JoinColumn(name = "student_id")

        private Student student;

        @ManyToOne
        @JoinColumn(name = "course_id")

        private Course course;

    }
}
