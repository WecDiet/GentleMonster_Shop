package com.gentlemonster.DTO.Requests.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressEmployeeRequest {
    @NotEmpty(message = "street is required not empty !")
    private String street;
    @NotEmpty(message = "ward is required not empty !")
    private String ward;
    @NotEmpty(message = "district is required not empty !")
    private String district;
    @NotEmpty(message = "city is required not empty !")
    private String city;
    @NotEmpty(message = "country is required not empty !")
    private String country;
}
