package com.gentlemonster.DTO.Requests.ProductType;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddProductTypeRequest {
    @NotEmpty(message = "Name Product type is required")
    private String name;
    private String description;
    private boolean status;
    private String slider;
    private BigInteger maxQuantity;
    private String category;
}
