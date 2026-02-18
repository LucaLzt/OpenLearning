package com.projects.openlearning.catalogue.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CoursePriceInfo(
        UUID id,
        BigDecimal
        price, boolean
        isPublished
) {
}
