package com.gentlemonster.GentleMonsterBE.Repositories;

import com.gentlemonster.GentleMonsterBE.Entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface IStoreRepository extends JpaRepository<Store, UUID>, JpaSpecificationExecutor<Store> {
    boolean existsByStoreName(String name);
    Optional<Store> findByStoreName(String name);
}
