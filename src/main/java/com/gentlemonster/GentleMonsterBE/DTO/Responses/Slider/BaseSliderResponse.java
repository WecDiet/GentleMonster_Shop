package com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.Category.CategoryResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Cloudinary.ImageResponse;

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
