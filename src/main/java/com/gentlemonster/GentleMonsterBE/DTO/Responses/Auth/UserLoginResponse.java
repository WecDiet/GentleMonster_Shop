package com.gentlemonster.GentleMonsterBE.DTO.Responses.Auth;

import com.gentlemonster.GentleMonsterBE.Entities.AuthToken;

import jakarta.persistence.Column;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {
    private String token; // token truy cập
    private String refreshToken; // token làm mới
    private String tokenType; // loại token,
    private String deviceToken;
    private String deviceName;
}
