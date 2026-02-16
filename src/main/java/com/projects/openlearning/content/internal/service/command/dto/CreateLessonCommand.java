package com.projects.openlearning.content.internal.service.command.dto;

import java.util.UUID;

public record CreateLessonCommand(
        String title,
        String videoUrl,
        String contextText,
        Integer orderIndex,
        UUID courseId,
        UUID sectionId,
        UUID instructorId
){
}
