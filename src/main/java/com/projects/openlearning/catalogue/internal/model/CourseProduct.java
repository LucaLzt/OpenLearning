package com.projects.openlearning.catalogue.internal.model;

import com.projects.openlearning.content.api.dto.PublicSection;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course_products")
public class CourseProduct {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "instructor_name", nullable = false)
    private String instructorName;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Instant publishedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<PublicSection> syllabus;

    public void updateInfo(String title, String description, BigDecimal price) {
        this.title = title != null ? title : this.title;
        this.description = description != null ? description : this.description;
        this.price = price != null ? price : this.price;
    }

}
