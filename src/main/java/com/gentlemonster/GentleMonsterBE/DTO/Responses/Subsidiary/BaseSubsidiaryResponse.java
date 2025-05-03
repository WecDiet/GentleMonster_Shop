package com.gentlemonster.GentleMonsterBE.DTO.Responses.Subsidiary;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.City.BaseCityResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseSubsidiaryResponse {
    private UUID id;
    private String companyName;
    private boolean status;
    private BaseCityResponse city;
}
