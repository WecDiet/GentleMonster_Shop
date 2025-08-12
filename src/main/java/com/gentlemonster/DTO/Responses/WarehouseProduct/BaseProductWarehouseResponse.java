package com.gentlemonster.DTO.Responses.WarehouseProduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gentlemonster.DTO.Responses.Warehouse.BaseWarehouseResponse;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseProductWarehouseResponse {
    private UUID id;
    private int quantity;
    private String manufacturer;

    private String productName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime importDate;

    private BigInteger totalImportPrice; // Tổng giá nhập hàng
    private BaseWarehouseResponse warehouse;
}
