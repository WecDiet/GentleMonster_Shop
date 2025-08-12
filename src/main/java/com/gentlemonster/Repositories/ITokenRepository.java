package com.gentlemonster.Repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gentlemonster.Entities.AuthToken;
import com.gentlemonster.Entities.User;

public interface ITokenRepository extends JpaRepository<AuthToken, UUID>, JpaSpecificationExecutor<AuthToken>{
    Optional<AuthToken> findByRefreshToken(String refreshToken);
    Optional<AuthToken> findByUserId(UUID userId);
    List<AuthToken> findByUser(User user);
}
