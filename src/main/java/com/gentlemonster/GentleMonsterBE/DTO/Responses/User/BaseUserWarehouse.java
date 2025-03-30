package com.gentlemonster.GentleMonsterBE.DTO.Responses.User;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseUserWarehouse {
    private String fullName;
    private String photoUrl;
    private String code;
}
