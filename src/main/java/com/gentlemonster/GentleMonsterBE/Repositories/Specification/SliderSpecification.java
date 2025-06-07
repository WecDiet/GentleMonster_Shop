package com.gentlemonster.GentleMonsterBE.Repositories.Specification;

import com.gentlemonster.GentleMonsterBE.Entities.Slider;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


public class SliderSpecification {

    public static Specification<Slider> getListSlider(String categorySlug) {
//        return (root, query, criteriaBuilder) -> {
//            if (categorySlug != null && !categorySlug.isEmpty()) {
//                // So sánh slug của trường category trong Slider
//                return criteriaBuilder.equal( // So sánh bằng giá trị của trường slug trong đối tượng Category của Slider với giá trị truyền vào từ client
//                        criteriaBuilder.lower(root.get("category").get("slug")), // Truy cập trường slug trong đối tượng Category của Slider
//                        categorySlug.toLowerCase().trim() // Chuyển đổi giá trị truyền vào thành chữ thường và loại bỏ khoảng trắng
//                );
//            }
//            return criteriaBuilder.conjunction();
//        };
        return (root, query, criteriaBuilder) -> {
            if (categorySlug != null && !categorySlug.isEmpty()) {
                // Kiểm tra status là true
                Predicate statusPredicate = criteriaBuilder.isTrue(root.get("status"));
                // So sánh slug của trường category trong Slider
                Predicate categoryPredicate = criteriaBuilder.equal( // So sánh bằng giá trị của trường slug trong đối tượng Category của Slider với giá trị truyền vào từ client
                        criteriaBuilder.lower(root.get("category").get("slug")), // Truy cập trường slug trong đối tượng Category của Slider
                        categorySlug.toLowerCase().trim() // Chuyển đổi giá trị truyền vào thành chữ thường và loại bỏ khoảng trắng
                );

                // Kết hợp cả hai điều kiện
                return criteriaBuilder.and(statusPredicate,categoryPredicate);
            }
            return criteriaBuilder.conjunction();
        };
    }
}
