package com.projects.openlearning.content.api.events;

import java.util.UUID;

public record CourseArchivedEvent(
        UUID courseId
) {
}
