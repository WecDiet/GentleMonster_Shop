package com.gentlemonster.GentleMonsterBE.DTO.Responses.User;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
    private String street;
    private String ward;
    private String district;
    private String city;
    private String country;
}
