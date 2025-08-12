package com.gentlemonster.Repositories;

import com.gentlemonster.Entities.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface IProductTypeRepository extends JpaRepository<ProductType, UUID>, JpaSpecificationExecutor<ProductType> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    Optional<ProductType> findByName(String name);
}
