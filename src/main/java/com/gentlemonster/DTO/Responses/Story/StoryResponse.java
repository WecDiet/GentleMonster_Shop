package com.gentlemonster.DTO.Responses.Story;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gentlemonster.DTO.Responses.Cloudinary.ImageResponse;
import com.gentlemonster.DTO.Responses.Product.Public.BaseProductPublicResponse;
import com.gentlemonster.DTO.Responses.ProductType.Public.ProductTypeOfStory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoryResponse {
    private String name;
    private String description;
    private boolean status;
    private String slug;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime modifiedDate;
    private ProductTypeOfStory productType;
    private List<ImageResponse> images;
}
