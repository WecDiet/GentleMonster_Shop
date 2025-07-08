package com.gentlemonster.GentleMonsterBE.DTO.Requests.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EditUserRequest extends AddressEmployeeRequest {
    @NotEmpty(message = "title is required not empty !")
    private String title;
    @NotEmpty(message = "First name is required")
    private String firstName;
    @NotEmpty(message = "middleName is required not empty !")
    private String middleName;
    @NotEmpty(message = "Last name is required")
    private String lastName;
    @NotEmpty(message = "Phone number is required")
    private String phoneNumber;
    private int day;
    private int month;
    private int year;
    @NotEmpty(message = "Password is required")
    private String password;
    private boolean status;
    @NotEmpty(message = "Role is required")
    private String role;
    @NotEmpty(message = "Subsidiary is required")
    private String store;
}
