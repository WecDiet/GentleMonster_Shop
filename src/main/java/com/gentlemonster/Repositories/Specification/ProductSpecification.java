package com.gentlemonster.Repositories.Specification;

import com.gentlemonster.Entities.Product;
import com.gentlemonster.Entities.Slider;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {

    public static Specification<Product> getListProduct(String categorySlug, String sliderSlug) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (categorySlug != null && !categorySlug.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(criteriaBuilder.lower(root.get("productType").get("category").get("slug")),categorySlug.toLowerCase().trim())
                );
            }
            if (sliderSlug != null && !sliderSlug.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(criteriaBuilder.lower(root.get("productType").get("slider").get("slug")), sliderSlug.toLowerCase().trim())
                );
            }
            return predicate; // Trả về điều kiện tổng hợp
        };
    }

    public static Specification<Product> getListProductByCategorySlug(String categorySlug) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (categorySlug != null && !categorySlug.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(criteriaBuilder.lower(root.get("productType").get("category").get("slug")), categorySlug.toLowerCase().trim())
                );
            }
            return predicate;
        };
    }

    // Thêm Specification để lấy ProductType theo Slider Slug
    public static Specification<Product> getListProductBySlider(String sliderSlug) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (sliderSlug != null && !sliderSlug.isEmpty()) {
                    predicate = criteriaBuilder.and(
                        predicate, criteriaBuilder.equal(criteriaBuilder.lower(root.get("productType").get("slider").get("slug")), sliderSlug.toLowerCase().trim())
                );
            }
            return predicate;
        };
    }

    public static Specification<Product> getListProductBySliders(List<Slider> sliders) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (sliders == null || sliders.isEmpty()) {
                return criteriaBuilder.disjunction(); // Trả về điều kiện sai nếu danh sách rỗng
            }
            // return root.get("slider").in(sliders);
            predicate = criteriaBuilder.and(predicate,
                    root.get("productType").get("slider").in(sliders)
            );
            return predicate;
        };
    }

    public static Specification<Product> getOneProductBySlugAndCode(String productSlug, String productCode) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (productSlug != null && !productSlug.trim().isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("slug")), "%" + productSlug.trim().toLowerCase() + "%")
                );
            }
            if (productCode != null && !productCode.trim().isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + productCode.trim().toLowerCase() + "%")
                    );
            }
            return predicate;
        };
    }

    public static Specification<Product> getProductByCode(List<String> code){
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (code != null && !code.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        root.get("code").in(code)
                );
            }
            return predicate;
        };
    }

}
