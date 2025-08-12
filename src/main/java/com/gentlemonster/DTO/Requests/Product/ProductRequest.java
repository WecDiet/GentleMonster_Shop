package com.gentlemonster.DTO.Requests.Product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private int limit = -1; // Giá trị mặc định là 10
    private int page = -1;   // Giá trị mặc định là 0
    private String name; // Không thể null
}
