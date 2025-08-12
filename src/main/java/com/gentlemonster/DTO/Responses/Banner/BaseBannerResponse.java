package com.gentlemonster.DTO.Responses.Banner;

import com.gentlemonster.DTO.Responses.Category.CategoryResponse;
import com.gentlemonster.DTO.Responses.Cloudinary.ImageResponse;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseBannerResponse {
    private UUID id;
    private String title;
    private int seq; // thứ tự banner từ 1 -> 4
    private CategoryResponse category;
    private ImageResponse media; 
}
