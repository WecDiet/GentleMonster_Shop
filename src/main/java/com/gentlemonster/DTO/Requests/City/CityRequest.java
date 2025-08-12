package com.gentlemonster.DTO.Requests.City;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityRequest {
    private int limit = -1; // Default value is -1
    private int page = -1;  // Default value is -1
}
