package com.projects.openlearning.enrollment.internal.service.command;

import com.projects.openlearning.catalogue.api.CourseLookupService;
import com.projects.openlearning.enrollment.internal.model.Enrollment;
import com.projects.openlearning.enrollment.internal.repository.EnrollmentRepository;
import com.projects.openlearning.enrollment.internal.service.command.dto.EnrollStudentCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollStudentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseLookupService courseLookupService;

    @Transactional
    public void enroll(EnrollStudentCommand command) {
        log.info("Creating enrollment for user: {} - course ID: {}", command.studentId(), command.courseId());

        // 1. Validate if the student is already enrolled in the course
        boolean alreadyEnrolled = enrollmentRepository.existsByUserIdAndCourseId(
                command.studentId(),
                command.courseId()
        );
        if (alreadyEnrolled) {
            throw new IllegalArgumentException("Student is already enrolled in the course with id: " + command.courseId());
        }

        // 2. Fetch course price info to ensure the course exists and is available for enrollment
        var courseInfo = courseLookupService.getCoursePriceInfo(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + command.courseId()));

        // 2. Create a new enrollment entity
        Enrollment enrollment = Enrollment.createNewEnrollment(
                command.studentId(),
                command.courseId(),
                courseInfo.price()
        );

        // 3. Save the enrollment to the repository
        enrollmentRepository.save(enrollment);

        log.info("Enrollment created for student: {} in course ID: {}", command.studentId(), command.courseId());
    }

}
