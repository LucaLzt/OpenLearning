package com.projects.openlearning.content.internal.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<Lesson> lessons = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return id != null && id.equals(section.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * Factory method to create a new Section with a specified order index.
     */
    public static Section createNewSection(Course course, String title, Integer orderIndex) {
        return Section.builder()
                .course(course)
                .title(title)
                .orderIndex(orderIndex)
                .build();
    }

    public void updateSection(String newTitle, Integer newOrderIndex) {
        this.title = newTitle;
        this.orderIndex = newOrderIndex;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.setSection(this);
    }

    public void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
        lesson.setSection(null);
    }
}
