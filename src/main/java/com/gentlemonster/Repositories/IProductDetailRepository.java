package com.gentlemonster.Repositories;

import com.gentlemonster.Entities.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface IProductDetailRepository extends JpaRepository<ProductDetail, UUID>, JpaSpecificationExecutor<ProductDetail> {
}
