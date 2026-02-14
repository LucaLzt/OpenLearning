package com.projects.openlearning.content.internal.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Column(name = "context_text", columnDefinition = "TEXT")
    private String contextText;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return id != null && id.equals(lesson.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * Factory method to create a new Lesson with a specified order index.
     */
    public static Lesson createNewLesson(Section section, String title, String videoUrl, String contextText, Integer orderIndex) {
        return Lesson.builder()
                .section(section)
                .title(title)
                .videoUrl(videoUrl)
                .contextText(contextText)
                .orderIndex(orderIndex)
                .build();
    }

    public void updateLesson(String newTitle, String newVideoUrl, String newContextText, Integer newOrderIndex) {
        this.title = newTitle != null ? newTitle : title;
        this.videoUrl = newVideoUrl != null ? newVideoUrl : videoUrl;
        this.contextText = newContextText != null ? newContextText : contextText;
        this.orderIndex = newOrderIndex != null ? newOrderIndex : orderIndex;
    }
}
