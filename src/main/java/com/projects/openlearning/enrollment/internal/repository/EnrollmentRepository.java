package com.projects.openlearning.enrollment.internal.repository;

import com.projects.openlearning.enrollment.internal.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    boolean existsByUserIdAndCourseId(UUID id, UUID courseId);

    List<Enrollment> findAllByUserId(UUID studentId);
}
