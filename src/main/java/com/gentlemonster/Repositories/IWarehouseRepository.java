package com.gentlemonster.Repositories;

import com.gentlemonster.Entities.User;
import com.gentlemonster.Entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface IWarehouseRepository extends JpaRepository<Warehouse, UUID>, JpaSpecificationExecutor<Warehouse> {
    boolean existsByWarehouseLocation(String warehouseLocation);
    boolean existsByWarehouseName(String warehouseName);
    // Optional<Warehouse> findByUser(User user);
    Optional<Warehouse> findByUsersContaining(User user);
    Optional<Warehouse> findByWarehouseName(String warehouseName);

}
