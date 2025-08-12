package com.gentlemonster.DTO.Requests.ProductType;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTypeRequest {
    private int limit = -1; // Giá trị mặc định là -1
    private int page = -1;   // Giá trị mặc định là -1
    private String name; // Không thể null
}
