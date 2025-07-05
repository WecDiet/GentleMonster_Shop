package com.gentlemonster.GentleMonsterBE.Repositories;

import com.gentlemonster.GentleMonsterBE.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IAuthRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String userName);
    Optional<User> findByGoogleAccountId(String googleAccountId);
    Optional<User> findByFacebookAccountId(String facebookAccountId);
//    Optional<User> findByResetToken(String resetToken);
}
