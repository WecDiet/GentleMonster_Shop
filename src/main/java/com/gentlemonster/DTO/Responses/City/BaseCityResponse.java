package com.gentlemonster.DTO.Responses.City;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import com.gentlemonster.DTO.Responses.Cloudinary.ImageResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseCityResponse {
    private UUID id;
    private String name;
    private String country;
    private String countrySlug;
    private ImageResponse image;
}
