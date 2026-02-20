package com.projects.openlearning.catalogue.internal.eventhandler;

import com.projects.openlearning.catalogue.internal.model.CourseProduct;
import com.projects.openlearning.catalogue.internal.repository.CourseProductRepository;
import com.projects.openlearning.content.api.events.CourseUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseUpdatedListener {

    private final CourseProductRepository productRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCourseUpdated(CourseUpdatedEvent event) {
        log.info("Received course updated event for course ID: {}", event.courseId());

        // 1. Fetch the existing CourseProduct from the repository
        CourseProduct courseProduct = productRepository.findById(event.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course product not found with id: " + event.courseId()));

        // 2. Update the CourseProduct details based on the event data
        courseProduct.updateInfo(
                event.title(),
                event.description(),
                event.price()
        );

        // 3. Save the updated CourseProduct back to the repository
        productRepository.save(courseProduct);
    }
}
