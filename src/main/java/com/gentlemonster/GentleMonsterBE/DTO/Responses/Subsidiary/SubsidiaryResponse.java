package com.gentlemonster.GentleMonsterBE.DTO.Responses.Subsidiary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.City.BaseCityResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.City.CityResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubsidiaryResponse {
    private String companyName;
    private String street;
    private String ward;
    private String district;
    private String hotLine;
    private String email;
    private boolean status;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime updatedAt;
    private CityResponse city;

}
