package com.gentlemonster.DTO.Responses.Product.Public;

import com.gentlemonster.DTO.Responses.Product.ProductDetailResponse;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailPublicResponse {
    private String name;
    private String description;
    private BigInteger price;
    private List<String> productImages;
    private ProductDetailResponse productDetail;
}
