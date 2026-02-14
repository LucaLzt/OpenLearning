package com.projects.openlearning.content.internal.repository;

import com.projects.openlearning.content.internal.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SectionRepository extends JpaRepository<Section, UUID> {
}
