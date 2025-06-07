package com.gentlemonster.GentleMonsterBE.Repositories;

import com.gentlemonster.GentleMonsterBE.Entities.WarehouseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface IWarehouseProductRepository extends JpaRepository<WarehouseProduct, UUID>, JpaSpecificationExecutor<WarehouseProduct> {
    boolean existsByProductName(String name);
    boolean existsByPublicProductFalse();
    Optional<WarehouseProduct> findByProductName(String name);
}
