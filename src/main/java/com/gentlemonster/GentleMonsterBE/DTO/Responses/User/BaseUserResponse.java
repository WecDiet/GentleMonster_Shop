package com.gentlemonster.GentleMonsterBE.DTO.Responses.User;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.Role.RoleResponse;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseUserResponse {
    private String id;
    private String fullName;
    private String email;
    private String createdDate;
    private RoleResponse role;
    private String photoUrl;
    private String slug;
}
