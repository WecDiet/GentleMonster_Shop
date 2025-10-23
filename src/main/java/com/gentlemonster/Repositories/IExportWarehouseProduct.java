package com.gentlemonster.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gentlemonster.Entities.ExportProduct;

public interface IExportWarehouseProduct
        extends JpaRepository<ExportProduct, UUID>, JpaSpecificationExecutor<ExportProduct> {

}
