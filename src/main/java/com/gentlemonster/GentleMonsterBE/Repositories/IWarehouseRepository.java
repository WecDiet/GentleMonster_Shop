package com.gentlemonster.GentleMonsterBE.Repositories;

import com.gentlemonster.GentleMonsterBE.Entities.User;
import com.gentlemonster.GentleMonsterBE.Entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface IWarehouseRepository extends JpaRepository<Warehouse, UUID>, JpaSpecificationExecutor<Warehouse> {
    boolean existsByWarehouseLocation(String warehouseLocation);
    boolean existsByWarehouseName(String warehouseName);
    Optional<Warehouse> findByUser(User user);
    Optional<Warehouse> findByWarehouseName(String warehouseName);

}
