package com.gentlemonster.GentleMonsterBE.DTO.Responses.Product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.ProductType.BaseProductTypeResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider.BaseSliderResponse;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private BigInteger price;
    private String thumbnail;
    private boolean status;
    private String slug;
    private String category;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime modifiedDate;
    private BaseProductTypeResponse productType;
    private List<ProductImageResponse> productImage;
    private ProductDetailResponse productDetail;
}
