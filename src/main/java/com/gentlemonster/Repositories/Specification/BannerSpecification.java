package com.gentlemonster.Repositories.Specification;

import com.gentlemonster.Entities.Banner;
import org.springframework.data.jpa.domain.Specification;

public class BannerSpecification {
    public static Specification<Banner> searchTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("title"), "%" + title + "%");
        };
    }

    public static Specification<Banner> filterBanner(String title) {
        return Specification.where(searchTitle(title));
    }
}
