package com.projects.openlearning.content.api;

import java.util.UUID;

public interface CourseAccessValidator {

    boolean hasActiveAccess(UUID userId, UUID courseId);

}
