package com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.ProductType.ProductTypePublicResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SliderPublicResponse {
    private UUID id;
    private String name;
    private String image;
    private String linkURL;
    private String slug;
    private ProductTypePublicResponse productType;
}
