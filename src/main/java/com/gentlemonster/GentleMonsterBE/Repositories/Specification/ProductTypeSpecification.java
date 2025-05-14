package com.gentlemonster.GentleMonsterBE.Repositories.Specification;

import com.gentlemonster.GentleMonsterBE.Entities.ProductType;
import com.gentlemonster.GentleMonsterBE.Entities.Slider;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

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
                        criteriaBuilder.equal(criteriaBuilder.lower(root.get("category").get("slug")), categorySlug.toLowerCase().trim())
                );
            }
            return predicate;
        };
    }

    // Thêm Specification để lấy ProductType theo Slider Slug
    public static Specification<ProductType> getListProductTypeBySlider(String sliderSlug) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (sliderSlug != null && !sliderSlug.isEmpty()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(criteriaBuilder.lower(root.get("slider").get("slug")), sliderSlug.toLowerCase().trim())
                );
            }
            return predicate;
        };
    }

    public static Specification<ProductType> getListProductTypeBySliders(List<Slider> sliders) {
        return (root, query, criteriaBuilder) -> {
            if (sliders == null || sliders.isEmpty()) {
                return criteriaBuilder.disjunction(); // Trả về điều kiện sai nếu danh sách rỗng
            }
            return root.get("slider").in(sliders);
        };
    }

}
