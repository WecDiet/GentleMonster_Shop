package com.gentlemonster.GentleMonsterBE.DTO.Responses.Role;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {
    private UUID id;
    private String name;
    private String description;
    private String createdDate;
    private String modifiedDate;
}

