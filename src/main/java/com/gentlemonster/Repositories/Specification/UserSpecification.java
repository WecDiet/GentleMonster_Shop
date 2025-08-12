package com.gentlemonster.Repositories.Specification;
import com.gentlemonster.Entities.Role;
import com.gentlemonster.Entities.User;
import com.gentlemonster.Utils.ValidationUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    private static ValidationUtils vietnameseStringUtils = new ValidationUtils();
    public static Specification<User> searchUser(String email, String name, String employeeCode) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // Khởi tạo một điều kiện "TRUE"

            if (email != null && !email.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase().trim() + "%"));
            }
            if (name != null && !name.isEmpty()) {
                String slugFormatName = name.trim().replace(" ", "+").toLowerCase();
                String slugStandardization = vietnameseStringUtils.removeAccents(slugFormatName).toLowerCase().trim();
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("slug")), "%" + slugStandardization + "%"));
            }
            if (employeeCode != null && !employeeCode.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("employeeCode")), "%" + employeeCode + "%"));
            }
            return predicate; // Trả về điều kiện tổng hợp
        };
    }

    public static Specification<User> byRoleName(String role) {
        return (root, query, criteriaBuilder) -> {
            if (role == null || role.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<User, Role> roleJoin = root.join("roles", JoinType.INNER);
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(roleJoin.get("name")),
                    role.toLowerCase()
            );
        };
    }

    public static Specification<User> filterUsers(String email, String name, String employeeCode) {
        return Specification.where(searchUser(email,name,employeeCode));
    }
}