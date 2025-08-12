package com.gentlemonster.DTO.Responses.Product;

import lombok.*;

import java.util.UUID;

import com.gentlemonster.DTO.Responses.Cloudinary.ImageResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseProductResponse {
    private UUID id;
    private String name;
    private boolean status;
    private ImageResponse thumbnailMedia;
}
