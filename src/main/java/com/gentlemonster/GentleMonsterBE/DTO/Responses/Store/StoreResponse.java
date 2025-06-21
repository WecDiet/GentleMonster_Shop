package com.gentlemonster.GentleMonsterBE.DTO.Responses.Store;

import java.util.List;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.City.BaseCityResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Cloudinary.ImageResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider.SliderResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponse {
    private String storeName;
    private String street;
    private String ward;
    private String district;
    private String hotLine;
    private String email;
    private String description;
    private boolean status;
    private String countrySlug;
    private BaseCityResponse city;
    private SliderResponse slider;
    private List<ImageResponse> images;
}
