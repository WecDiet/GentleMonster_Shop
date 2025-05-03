package com.gentlemonster.GentleMonsterBE.DTO.Responses.City;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityResponse {
    private UUID id;
    private String cityName;
    private String slug;
    private String thumbnail;
    private boolean status;
    private String country;
}
