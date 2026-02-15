package com.projects.openlearning.content.internal.web.dto;

import com.projects.openlearning.content.internal.model.CourseStatus;

public record UpdateCourseStatusRequest(
        CourseStatus status
) {
}
