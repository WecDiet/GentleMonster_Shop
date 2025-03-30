package com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    @NotEmpty(message = "Email is required not empty !")
    private String email;
    @NotEmpty(message = "Confirm Email is required not empty !")
    private String confirmEmail;
    @NotEmpty(message = "Password is required not empty !")
    private String password;
    @NotEmpty(message = "Confirm Password is required not empty !")
    private String confirmPassword;
    @NotEmpty(message = "Title is required not empty !")
    private String title;
    @NotEmpty(message = "First Name is required not empty !")
    private String firstName;
    @NotEmpty(message = "Middle Name is required not empty !")
    private String middleName;
    @NotEmpty(message = "Last Name is required not empty !")
    private String lastName;
    @Min(value = 1, message = "Day must be valid!")
    @Max(value = 31, message = "Day must be valid!")
    private int dayBirthDay;
    @Min(value = 1, message = "Month must be valid!")
    @Max(value = 12, message = "Month must be valid!")
    private int monthBirthDay;
    @Min(value = 1900, message = "Year must be valid!")
    @Max(value = 2024, message = "Year must be valid!")
    private int yearBirthDay;
}
