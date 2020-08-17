package tables;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class SubscriptionID implements Serializable {
    @Column(name = "student_id")
    private int student;
    @Column(name = "course_id")
    private int course;

    private SubscriptionID() {
    }

    public SubscriptionID(int studentId, int courseId) {
        this.student = studentId;
        this.course = courseId;
    }
}