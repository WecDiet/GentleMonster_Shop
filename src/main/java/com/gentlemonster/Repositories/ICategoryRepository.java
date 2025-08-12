package com.gentlemonster.Repositories;

import com.gentlemonster.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ICategoryRepository extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {
    boolean existsByName(String name);
    Optional<Category> findByName(String name);
    Optional<Category> findBySlug(String slug);
}
