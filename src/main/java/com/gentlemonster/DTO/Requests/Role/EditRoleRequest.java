package com.gentlemonster.DTO.Requests.Role;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditRoleRequest {
    @NotEmpty(message = "name is required not empty !")
    private String name;
    private String description;
}
