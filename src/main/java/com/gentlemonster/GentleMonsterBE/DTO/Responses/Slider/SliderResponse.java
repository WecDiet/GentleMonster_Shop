package com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.Category.CategoryResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SliderResponse {
    private UUID id;
    private String name;
    private String image;
    private boolean status;
    private boolean highlighted;
    private String linkURL;
    private String slug;
    private String createdDate;
    private String modifiedDate;
    private CategoryResponse category;
}
