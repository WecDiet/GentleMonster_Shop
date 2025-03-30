package com.gentlemonster.GentleMonsterBE.DTO.Requests.Role;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleRequest {
    private int page = -1;
    private int limit = -1;
    @NotEmpty(message = "Role name is required")
    private String name;
}
