package com.projects.openlearning.enrollment.internal.model;

public enum EnrollmentStatus {
    // MVP: The enrollment process is simplified to only include APPROVED and REFUNDED statuses.
    // In a more complex implementation, we might have additional statuses such as PENDING, REJECTED, etc.
    APPROVED,
    REFUNDED
}
