package com.projects.openlearning.catalogue.internal.eventhandler;

import com.projects.openlearning.catalogue.internal.repository.CourseProductRepository;
import com.projects.openlearning.content.api.events.CourseArchivedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseArchivedListener {

    private final CourseProductRepository productRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCourseArchived(CourseArchivedEvent event) {
        log.info("Received course archived event for course ID: {}", event.courseId());

        // 1. Delete the CourseProduct from the repository based on the course ID
        productRepository.deleteById(event.courseId());

        log.info("Course product with ID: {} has been removed from the catalogue", event.courseId());
    }
}
