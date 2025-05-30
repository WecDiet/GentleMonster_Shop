package com.gentlemonster.GentleMonsterBE.DTO.Responses.City;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseCityResponse {
    private UUID id;
    private String name;
    private String country;
    private String countrySlug;
}
