package com.gentlemonster.DTO.Responses.Slider;

import com.gentlemonster.DTO.Responses.Cloudinary.ImageResponse;
import com.gentlemonster.DTO.Responses.ProductType.ProductTypePublicResponse;
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
    private String linkURL;
    private String slug;
    private ImageResponse image;
    private ProductTypePublicResponse productType;
}
