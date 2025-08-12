package com.gentlemonster.DTO.Responses.Product;

import lombok.*;

import java.util.List;
import java.util.UUID;

import com.gentlemonster.DTO.Responses.Cloudinary.ImageResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {
    private UUID id;
    private String frame;
    private String lens;
    private String shape;
    private String material;
    private double lens_Width;
    private double lens_Height;
    private double bridge;
    private String country;
    private String manufacturer;
    private List<ImageResponse> images;
}
