package com.gentlemonster.DTO.Responses.Store;

import com.gentlemonster.DTO.Responses.City.BaseCityResponse;
import com.gentlemonster.DTO.Responses.Cloudinary.ImageResponse;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseStoreResponse {
    private UUID id;
    private String storeName;
    private BaseCityResponse city;
    private ImageResponse thumbnailMedia;
}
