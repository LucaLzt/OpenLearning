package com.projects.openlearning.content.internal.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
@EntityListeners(AuditingEntityListener.class)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "instructor_id", nullable = false)
    private UUID instructorId;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;

    @LastModifiedDate
    @Column(name = "last_updated")
    private Instant updatedAt;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<Section> sections = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id != null && id.equals(course.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * Factory method to create a new Course in DRAFT status.
     */
    public static Course createNewCourse(UUID instructorId, String title, String description) {
        return Course.builder()
                .instructorId(instructorId)
                .title(title)
                .description(description)
                .status(CourseStatus.DRAFT)
                .build();
    }

    public void publish() {
        this.status = CourseStatus.PUBLISHED;
    }

    public void updateCourse(String newTitle, String newDescription) {
        this.title = newTitle != null ? newTitle : this.title;
        this.description = newDescription != null ? newDescription : this.description;
    }

    public void addSection(Section section) {
        sections.add(section);
        section.setCourse(this);
    }

    public void removeSection(Section section) {
        sections.remove(section);
        section.setCourse(null);
    }
}