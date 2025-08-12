package com.gentlemonster.DTO.Requests.WarehouseProduct;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseProductRequest {
    private int limit = -1; // Giá trị mặc định là 10
    private int page = -1;   // Giá trị mặc định là 0
    private String name; // Không thể null
}
