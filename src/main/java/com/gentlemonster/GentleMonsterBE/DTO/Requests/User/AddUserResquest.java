package com.gentlemonster.GentleMonsterBE.DTO.Requests.User;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserResquest extends AddressUserRequest {
    @NotEmpty(message = "title is required not empty !")
    private String title;
    @NotEmpty(message = "firstName is required not empty !")
    private String firstName;
    @NotEmpty(message = "middleName is required not empty !")
    private String middleName;
    @NotEmpty(message = "lastName is required not empty !")
    private String lastName;
    @NotEmpty(message = "email is required not empty !")
    private String email;
    @NotEmpty(message = "Gender is required not empty !")
    private String gender;
    @NotEmpty(message = "phoneNumber is required not empty !")
    @Size(min = 10, max = 10, message = "phoneNumber must be 10 characters")
    private String phoneNumber;
    private int day;
    private int month;
    private int year;
    @NotEmpty(message = "password is required not empty !")
    private String password;
    @NotEmpty(message = "photos is required not empty !")
    private String photoUrl;
    private boolean status;
    @NotEmpty(message = "role is required not empty !")
    private String role;
    @NotEmpty(message = "subsidiary is required not empty !")
    private String subsidiary;
}
