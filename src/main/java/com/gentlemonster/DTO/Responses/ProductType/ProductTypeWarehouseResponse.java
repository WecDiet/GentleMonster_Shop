package com.gentlemonster.DTO.Responses.ProductType;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductTypeWarehouseResponse {
    private String name;
    private String slug;
    private boolean status;
}
