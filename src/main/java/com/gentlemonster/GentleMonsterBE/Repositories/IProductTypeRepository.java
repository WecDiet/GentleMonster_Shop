package com.gentlemonster.GentleMonsterBE.Repositories;

import com.gentlemonster.GentleMonsterBE.Entities.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductTypeRepository extends JpaRepository<ProductType, UUID>, JpaSpecificationExecutor<ProductType> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    Optional<ProductType> findByName(String name);
//    List<ProductType> findBySliderSlug(String sliderSlug);
    List<ProductType> findAllByCategory_Slug(String categorySlug);
    List<ProductType> findAllByCategorySlugAndSliderSlug(String categorySlug, String sliderSlug);
}
