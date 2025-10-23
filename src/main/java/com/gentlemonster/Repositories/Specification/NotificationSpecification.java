package com.gentlemonster.Repositories.Specification;

import java.util.UUID;


import org.springframework.data.jpa.domain.Specification;

import com.gentlemonster.Entities.Notification;
import com.gentlemonster.Entities.User;

import jakarta.persistence.criteria.Join;

public class NotificationSpecification {
    public static Specification<Notification> hasUserID (String userId){
        return (root, query, criteriaBuilder) -> {
            if (userId == null || userId.isEmpty()) {
                return criteriaBuilder.conjunction();  
            }
            Join<Notification, User> userJoin = root.join("users");
            return criteriaBuilder.equal(userJoin.get("id"), UUID.fromString(userId));
        };
    }


    public static Specification<Notification> searchByKeyword(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();  
            }
            return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + search.toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("body")), "%" + search.toLowerCase() + "%")
            );
        };
    }

    public static Specification<Notification> filtersNotification(String userId, String search){
        return Specification.where(hasUserID(userId))
                .and(searchByKeyword(search));
    }
}
