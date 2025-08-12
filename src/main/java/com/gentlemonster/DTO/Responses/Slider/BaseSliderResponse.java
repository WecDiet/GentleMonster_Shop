package com.gentlemonster.DTO.Responses.Slider;

import com.gentlemonster.DTO.Responses.Category.CategoryResponse;
import com.gentlemonster.DTO.Responses.Cloudinary.ImageResponse;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseSliderResponse {
    private UUID id;
    private String name;
    private String slug;
    private ImageResponse thumbnailMedia;
}
