package com.gentlemonster.DTO.Responses.Story.Public;
import java.util.List;

import com.gentlemonster.DTO.Responses.Cloudinary.ImageResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseStoryPublicResponse {
    private String name;
    private ImageResponse thumbnailMedia;
}
