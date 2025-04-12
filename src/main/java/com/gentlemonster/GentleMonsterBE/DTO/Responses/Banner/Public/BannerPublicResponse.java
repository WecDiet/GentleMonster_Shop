package com.gentlemonster.GentleMonsterBE.DTO.Responses.Banner.Public;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerPublicResponse {
    private String image;  // Đường dẫn ảnh banner
    private String title;
    private String link;
}
