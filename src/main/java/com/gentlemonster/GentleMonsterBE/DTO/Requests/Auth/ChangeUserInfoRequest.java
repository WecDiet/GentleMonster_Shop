package com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserInfoRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private String gender;
    private int day;
    private int month;
    private int year;
    private String title;
}
