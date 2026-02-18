package com.projects.openlearning.content.api.dto;

import java.util.List;

public record PublicSection(
        String title,
        Integer orderIndex,
        List<PublicLesson> lessons
) {
}
