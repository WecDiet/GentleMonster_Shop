package com.gentlemonster.DTO.Requests.City;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCityRequest {
    private String name;
    private boolean status;
    private String country;
}
