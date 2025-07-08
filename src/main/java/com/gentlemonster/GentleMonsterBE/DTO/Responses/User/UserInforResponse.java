package com.gentlemonster.GentleMonsterBE.DTO.Responses.User;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.Cloudinary.ImageResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInforResponse {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String gender;
    private String phoneNumber;
    private String birthDay;
    private String password;
    private ImageResponse image;
}
