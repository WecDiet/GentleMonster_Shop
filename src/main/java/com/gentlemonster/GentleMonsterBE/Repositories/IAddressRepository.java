package com.gentlemonster.GentleMonsterBE.Repositories;

import com.gentlemonster.GentleMonsterBE.Entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface  IAddressRepository extends JpaRepository<Address, UUID>, JpaSpecificationExecutor<Address> {
    boolean existsByCity(String city);
    Optional<Address> findByCity(String city);
}
