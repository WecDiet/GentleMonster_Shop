package com.gentlemonster.Repositories.Specification;

import org.springframework.data.jpa.domain.Specification;

import com.gentlemonster.Entities.AuthToken;
import com.gentlemonster.Entities.User;

public class TokenSpecification {

    public static Specification<AuthToken> tokenSpec(User user, String tokenType, String deviceToken, String deviceName){
        return (root, query, criteriaBuilder) -> {
            if (user != null) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("user"), user),
                        criteriaBuilder.equal(root.get("tokenType"), tokenType),
                        criteriaBuilder.equal(root.get("deviceToken"), deviceToken),
                        criteriaBuilder.equal(root.get("deviceName"), deviceName)
                );
            } else {
                return criteriaBuilder.disjunction(); // Trả về điều kiện sai nếu user là null
            }
        };
    }
    
}
