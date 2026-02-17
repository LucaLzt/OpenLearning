package com.projects.openlearning.catalogue.internal.eventhandler;

import com.projects.openlearning.catalogue.internal.model.CourseProduct;
import com.projects.openlearning.catalogue.internal.repository.CourseProductRepository;
import com.projects.openlearning.content.api.events.CoursePublishedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoursePublishedListener {

    private final CourseProductRepository productRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCoursePublished(CoursePublishedEvent event) {
        log.info("Catalogue received new course: {} - {}", event.courseId(), event.title());

        // 1. Create a new CourseProduct entity based on the event data
        var courseProduct = new CourseProduct(
                event.courseId(),
                event.title(),
                event.description(),
                event.instructorName(),
                event.price(),
                event.publishedAt(),
                event.syllabus()
        );

        // 2. Save the new CourseProduct to the repository
        productRepository.save(courseProduct);

        log.info("Course product created in catalogue for course ID: {}", event.courseId());
    }
}
