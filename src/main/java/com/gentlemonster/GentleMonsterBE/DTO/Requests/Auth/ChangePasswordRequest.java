package com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth;


import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    private String currentPassword;
    private String confirmCurrentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
