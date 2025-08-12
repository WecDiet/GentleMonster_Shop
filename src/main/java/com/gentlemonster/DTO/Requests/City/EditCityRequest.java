package com.gentlemonster.DTO.Requests.City;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditCityRequest {
    private String cityName;
    private String thumbnail;
    private boolean status;
    private String country;
}
