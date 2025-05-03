package com.gentlemonster.GentleMonsterBE.DTO.Requests.Subsidiary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditSubsidiaryRequest {
    private String companyName;
    private String street;
    private String ward;
    private String district;
    private String hotLine;
    private String email;
    private boolean status;
    private String description;
    private String city;
}
