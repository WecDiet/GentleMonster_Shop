package com.gentlemonster.GentleMonsterBE.DTO.Responses.Story.Public;

import java.util.List;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.Cloudinary.ImageResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.ProductType.Public.ProductTypeOfStory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoryPublicResponse {
    private String name;
    private String description;
    private List<ImageResponse> images;
    private ProductTypeOfStory productType;
}
