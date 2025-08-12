package com.gentlemonster.Repositories;

import com.gentlemonster.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface IRoleRepository extends JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);
    Optional<Role> findByName(String name);
}
