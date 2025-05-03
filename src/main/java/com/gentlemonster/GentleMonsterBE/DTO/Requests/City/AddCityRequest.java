package com.gentlemonster.GentleMonsterBE.DTO.Requests.City;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCityRequest {
    private String cityName;
    private String thumbnail;
    private boolean status;
    private String country;
}
