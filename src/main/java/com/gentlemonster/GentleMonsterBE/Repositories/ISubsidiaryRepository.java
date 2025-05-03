package com.gentlemonster.GentleMonsterBE.Repositories;

import com.gentlemonster.GentleMonsterBE.Entities.Subsidiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ISubsidiaryRepository extends JpaRepository<Subsidiary, UUID>, JpaSpecificationExecutor<Subsidiary> {
    boolean existsByCompanyName(String companyName);
    boolean existsBySlug(String slug);
    Optional<Subsidiary> findByCompanyName(String companyName);
}
