package com.projects.openlearning.content.api.events.dto;

import java.util.List;

public record PublicSection(
        String title,
        Integer orderIndex,
        List<PublicLesson> lessons
) {
}
