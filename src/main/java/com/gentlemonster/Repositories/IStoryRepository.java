package com.gentlemonster.Repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gentlemonster.Entities.Story;

public interface IStoryRepository extends JpaRepository<Story, UUID>, JpaSpecificationExecutor<Story> {
    boolean existsByName(String name);
    Optional<Story> findByName(String name);
    Optional<Story> findBySlug(String slug);
}
