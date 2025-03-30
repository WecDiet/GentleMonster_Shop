package com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserInfoRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;

}
