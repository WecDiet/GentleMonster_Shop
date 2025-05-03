package com.gentlemonster.GentleMonsterBE.DTO.Requests.Subsidiary;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubsidiaryRequest {
    private int limit = -1; // Default value is -1
    private int page = -1;  // Default value is -1
}
