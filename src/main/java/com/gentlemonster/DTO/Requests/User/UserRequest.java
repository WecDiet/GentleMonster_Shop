package com.gentlemonster.DTO.Requests.User;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private int limit = -1; // Giá trị mặc định là 10
    private int page = -1;   // Giá trị mặc định là 0
    private String name; // Không thể null
    private String email;    // Không thể null
    private String code; // Có thể null
    private String role;
}
