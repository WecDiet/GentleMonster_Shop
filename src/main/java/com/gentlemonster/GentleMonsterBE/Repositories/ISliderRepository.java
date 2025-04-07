package com.gentlemonster.GentleMonsterBE.Repositories;

import com.gentlemonster.GentleMonsterBE.Entities.Slider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ISliderRepository extends JpaRepository<Slider, UUID>, JpaSpecificationExecutor<Slider> {
    boolean existsByImage(String image);
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
    boolean existsByHighlighted(boolean highlighted);
    Optional<Slider> findByName(String name);
    Optional<Slider> findBySlug(String slug);
}
