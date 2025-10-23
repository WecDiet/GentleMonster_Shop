package com.gentlemonster.DTO.Requests.User;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserResquest extends AddressEmployeeRequest {
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
    @Size(min = 10, max = 12, message = "phoneNumber must be 10 characters")
    private String phoneNumber;
    @NotEmpty(message = "position is required not empty !")
    private String position;
    private int day;
    private int month;
    private int year;
    @NotEmpty(message = "password is required not empty !")
    private String password;
    @NotEmpty(message = "username is required not empty !")
    private String username;
    private boolean status;
    @NotEmpty(message = "role is required not empty !")
    private String role;

    private String store;
}
