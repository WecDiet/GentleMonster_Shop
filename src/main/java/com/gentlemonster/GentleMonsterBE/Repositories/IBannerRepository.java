package com.gentlemonster.GentleMonsterBE.Repositories;

import com.gentlemonster.GentleMonsterBE.Entities.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IBannerRepository extends JpaRepository<Banner, UUID>, JpaSpecificationExecutor<Banner> {
    boolean existsByTitle(String title);
    Optional<Banner> findByTitle(String title);
    List<Banner> findByStatusTrue();
}
