package com.projects.openlearning.enrollment.internal.service.provider;

import com.projects.openlearning.content.api.CourseAccessValidator;
import com.projects.openlearning.enrollment.internal.model.EnrollmentStatus;
import com.projects.openlearning.enrollment.internal.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentAccessValidatorImpl implements CourseAccessValidator {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public boolean hasActiveAccess(UUID userId, UUID courseID) {
        log.info("Checking if enrollment with id {} has access", courseID);
        return enrollmentRepository.existsByUserIdAndCourseIdAndStatus(userId, courseID, EnrollmentStatus.APPROVED);
    }
}
