package com.gentlemonster.DTO.Responses.Auth;

import lombok.*;

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
