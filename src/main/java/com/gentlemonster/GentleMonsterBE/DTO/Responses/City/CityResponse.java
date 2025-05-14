package com.gentlemonster.GentleMonsterBE.DTO.Responses.City;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityResponse {
    private UUID id;
    private String name;
    private String slug;
    private String thumbnail;
    private boolean status;
    private String country;
    private String countrySlug;
}
