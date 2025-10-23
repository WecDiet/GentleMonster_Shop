package com.gentlemonster.Repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gentlemonster.Entities.Voucher;

public interface IVoucherRepository extends JpaRepository<Voucher, UUID>, JpaSpecificationExecutor<Voucher> {
    boolean existsByCode(String code);
    Optional<Voucher> findByCode(String code);
    
}
