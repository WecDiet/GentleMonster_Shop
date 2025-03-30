package com.gentlemonster.GentleMonsterBE.DTO.Responses.Auth;



import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {
    private List<AddressResponse> addresses;
}
