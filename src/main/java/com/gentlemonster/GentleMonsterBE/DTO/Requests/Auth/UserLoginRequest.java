package com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    @NotEmpty(message = "Email is required not empty !")
    private String email;
    @NotEmpty(message = "Password is required not empty !")
    private String password;
    private String deviceToken;
}
