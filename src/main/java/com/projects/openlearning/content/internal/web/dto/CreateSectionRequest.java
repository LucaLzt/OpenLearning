package com.projects.openlearning.content.internal.web.dto;

public record CreateSectionRequest(
        String title,
        Integer orderIndex
) {
}
