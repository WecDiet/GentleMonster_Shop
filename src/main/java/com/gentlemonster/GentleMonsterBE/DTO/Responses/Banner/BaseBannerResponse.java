package com.gentlemonster.GentleMonsterBE.DTO.Responses.Banner;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.Category.CategoryResponse;
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
    private String thumbnail;
    private int seq; // thứ tự banner từ 1 -> 4
    private CategoryResponse category;
}
