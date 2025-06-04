package com.gentlemonster.GentleMonsterBE.Repositories;

import com.gentlemonster.GentleMonsterBE.Entities.Category;
import com.gentlemonster.GentleMonsterBE.Entities.Slider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ISliderRepository extends JpaRepository<Slider, UUID>, JpaSpecificationExecutor<Slider> {
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
    boolean existsByHighlighted(boolean highlighted);
    Optional<Slider> findByName(String name);
    Optional<Slider> findBySlug(String slug);
    List<Slider> findByCategory(Category category);
}
