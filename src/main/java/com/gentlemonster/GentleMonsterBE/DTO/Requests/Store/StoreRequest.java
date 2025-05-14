package com.gentlemonster.GentleMonsterBE.DTO.Requests.Store;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreRequest {
    private int limit = -1; // Giá trị mặc định là 10
    private int page = -1;   // Giá trị mặc định là 0
    @NotEmpty(message = "Country slug is required")
    String country;
//    @NotEmpty(message = "City name is required")
    String city;
}
