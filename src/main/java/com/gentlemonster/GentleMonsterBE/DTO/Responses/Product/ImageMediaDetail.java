package com.gentlemonster.GentleMonsterBE.DTO.Responses.Product;

import java.util.List;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.Cloudinary.ImageResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageMediaDetail {
    private List<ImageResponse> images;
}
