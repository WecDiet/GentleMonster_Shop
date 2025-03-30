package com.gentlemonster.GentleMonsterBE.DTO.Responses.User;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.Role.RoleResponse;
import com.gentlemonster.GentleMonsterBE.Entities.Role;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String slug;
    private String createdDate;
    private String email;
    private String code;
    private String gender;
    private String phoneNumber;
    private List<AddressResponse> addresses;
    private String birthDay;
    private String password;
    private String photoUrl;
    private boolean status;
    private RoleResponse role;
}
