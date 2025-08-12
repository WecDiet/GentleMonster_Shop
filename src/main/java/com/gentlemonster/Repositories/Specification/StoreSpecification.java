package com.gentlemonster.Repositories.Specification;

import com.gentlemonster.Entities.Store;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class StoreSpecification {
    public static Specification<Store> getStoreByCountryOrCity(String country, String city){
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // Khởi tạo một điều kiện "TRUE"
            if (country != null && !country.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(criteriaBuilder.lower(root.get("city").get("countrySlug")), country.toLowerCase().trim()));
            }
            if (city != null && !city.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("city").get("name")), "%" +city.toLowerCase().trim()+"%" ));
            }
            return predicate;
        };
    }


    public static Specification<Store> filterStore(String countrySlug, String cityName) {
        return Specification.where(getStoreByCountryOrCity(countrySlug, cityName));
    }
}
