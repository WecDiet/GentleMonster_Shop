package com.gentlemonster.GentleMonsterBE.Repositories.Specification;

import com.gentlemonster.GentleMonsterBE.Entities.ProductType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductTypeSpecification {

    public static Specification<ProductType> getListProductType(String categorySlug, String sliderSlug) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (categorySlug != null && !categorySlug.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(criteriaBuilder.lower(root.get("category").get("slug")),categorySlug.toLowerCase().trim())
                );
            }
            if (sliderSlug != null && !sliderSlug.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(criteriaBuilder.lower(root.get("slider").get("slug")), sliderSlug.toLowerCase().trim())
                );
            }
            return predicate; // Trả về điều kiện tổng hợp
        };
    }

    public static Specification<ProductType> getListProductTypeByCategorySlug(String categorySlug) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (categorySlug != null && !categorySlug.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("category").get("slug")), categorySlug.toLowerCase().trim())
                );
            }
            return predicate;
        };
    }

//    public static Specification<Product> filterProduct(String categorySlug, String sliderSlug) {
//        return Specification.where(getListProduct(categorySlug, sliderSlug)
//                .and(getListProductByCategorySlug(categorySlug))
//        );
//    }
}
