package com.gentlemonster.GentleMonsterBE.Repositories;

import com.gentlemonster.GentleMonsterBE.Entities.Collaboration;
import com.gentlemonster.GentleMonsterBE.Entities.Slider;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ICollaborationRepository extends JpaRepository<Collaboration, UUID>, JpaSpecificationExecutor<Collaboration> {
    @Transactional
    void deleteBySlider(Slider slider);
    // @Transactional
    // void deleteByStoryId(String storyId);
    Optional<Collaboration> findBySlider(Slider slider);
}
