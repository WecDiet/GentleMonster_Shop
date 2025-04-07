package com.gentlemonster.GentleMonsterBE.DTO.Responses.ProductType.Public;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.Public.BaseProductPublicResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPublicResponse {
    private String name;
    private List<BaseProductPublicResponse> products;
}
