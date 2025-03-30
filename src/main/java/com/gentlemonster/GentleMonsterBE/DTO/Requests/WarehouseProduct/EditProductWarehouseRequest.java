package com.gentlemonster.GentleMonsterBE.DTO.Requests.WarehouseProduct;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditProductWarehouseRequest {
    @NotEmpty(message = "Product name is required")
    private String productName;
    @NotEmpty(message = "Product type is required")
    private String productType;
    @NotEmpty(message = "Warehouse name is required")
    private String wareHouseName;

    private BigInteger quantity;

    private BigInteger importPrice; // Giá nhập hàng

    @NotEmpty(message = "Manufacturer is required")
    private String manufacturer;

    private int importDay;
    private int importMonth;
    private int importYear;
    private boolean publicProduct;
}
