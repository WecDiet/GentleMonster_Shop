package com.gentlemonster.DTO.Responses.City;

import lombok.*;

import java.util.UUID;

import com.gentlemonster.DTO.Responses.Cloudinary.ImageResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityResponse {
    private UUID id;
    private String name;
    private String slug;
    private boolean status;
    private String country;
    private String countrySlug;
    private ImageResponse image;
}
