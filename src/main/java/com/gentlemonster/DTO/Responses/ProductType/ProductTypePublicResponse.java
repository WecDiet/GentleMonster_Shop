package com.gentlemonster.DTO.Responses.ProductType;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductTypePublicResponse {
    private String id;
    private String name;
    private String slug;
}
