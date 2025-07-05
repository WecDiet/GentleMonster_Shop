package com.gentlemonster.GentleMonsterBE.Repositories;

import com.gentlemonster.GentleMonsterBE.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsBySlug(String name);
    boolean existsByCode(String code);
    boolean existsByFullName(String fullName);
    boolean existsByFullNameAndCode(String fullName, String code);
    boolean existsByEmailAndSlug(String email, String slug);
    Optional<User> findByEmail(String email);
    Optional<User> findByFullNameAndCode(String fullName, String code);
    Optional<User> findByUsername(String userName);
}
