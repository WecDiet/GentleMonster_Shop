package com.gentlemonster.DTO.Responses.ProductType;

import com.gentlemonster.DTO.Responses.Slider.BaseSliderResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductTypeResponse {
    private String name;
    private String description;
    private String slug;
    private boolean status;
    private String linkURL;
    private BaseSliderResponse slider;
}
