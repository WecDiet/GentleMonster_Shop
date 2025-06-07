package com.gentlemonster.GentleMonsterBE.DTO.Responses.ProductType.Public;

import java.util.List;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.Public.BaseProductPublicResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeOfStory {
    private List<BaseProductPublicResponse> products;
}
