package com.projects.openlearning.catalogue.internal.repository;

import com.projects.openlearning.catalogue.internal.model.CourseProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseProductRepository extends JpaRepository<CourseProduct, UUID> {

    Page<CourseProduct> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<CourseProduct> findAllByIdIn(List<UUID> ids);

}
