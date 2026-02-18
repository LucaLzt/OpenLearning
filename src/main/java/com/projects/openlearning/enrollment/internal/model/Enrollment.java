package com.projects.openlearning.enrollment.internal.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enrollments")
@EntityListeners(AuditingEntityListener.class)
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "course_id", nullable = false)
    private UUID courseId;

    @CreatedDate
    @Column(name = "enrollment_date", nullable = false, updatable = false)
    private Instant enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status;

    @Column(name = "amount_paid", nullable = false)
    private BigDecimal amountPaid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * Factory method to create a new enrollment with default status and current enrollment date.
     */
    public static Enrollment createNewEnrollment(UUID userId, UUID courseId, BigDecimal amountPaid) {
        return Enrollment.builder()
                .userId(userId)
                .courseId(courseId)
                .enrollmentDate(Instant.now())
                .status(EnrollmentStatus.APPROVED)
                .amountPaid(amountPaid)
                .build();
    }
}
