package com.gentlemonster.GentleMonsterBE.DTO.Responses.ProductType;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.Public.BaseProductPublicResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseProductTypeResponse {
    private String id;
    private String name;
    private String slug;
    private boolean status;
    private String linkURL;
//    private List<BaseProductPublicResponse> products;
}
