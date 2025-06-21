package com.gentlemonster.GentleMonsterBE.DTO.Responses.Banner.Public;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.Cloudinary.ImageResponse;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerPublicResponse {
    private ImageResponse media; 
    private String title;
    private String link;
}
