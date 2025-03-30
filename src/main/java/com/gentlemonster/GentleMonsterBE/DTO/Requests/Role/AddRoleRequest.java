package com.gentlemonster.GentleMonsterBE.DTO.Requests.Role;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddRoleRequest {
    private String name; // Role name NOT NULL
    private String description;
}
