package com.gentlemonster.GentleMonsterBE.Repositories;

import com.gentlemonster.GentleMonsterBE.Entities.Product;
import com.gentlemonster.GentleMonsterBE.Entities.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    boolean existsByProductType(ProductType productType);
    boolean existsByProductTypeAndName(ProductType productType, String name);
    Optional<Product> findByName(String name);
    Optional<Product> findBySlug(String slug);
//    List<Product> findByWarehouseActiveTrue();
}
