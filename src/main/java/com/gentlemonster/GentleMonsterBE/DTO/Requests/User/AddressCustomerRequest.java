package com.gentlemonster.GentleMonsterBE.DTO.Requests.User;

import jakarta.validation.constraints.NotEmpty;
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
public class AddressCustomerRequest {
    @NotEmpty(message = "street is required not empty !")
    private String street;
    @NotEmpty(message = "ward is required not empty !")
    private String ward;
    @NotEmpty(message = "district is required not empty !")
    private String district;
    @NotEmpty(message = "city is required not empty !")
    private String city;
    @NotEmpty(message = "country is required not empty !")
    private String country;
    @NotEmpty(message = "name is required not empty !")
    private String name; // Tên người nhận địa chỉ
    @NotEmpty(message = "phoneNumber is required not empty !")
    private String phoneNumber; // Số điện thoại người nhận địa chỉ
    
    private boolean type; // Loại địa chỉ (ví dụ: "home", "office
    
    private boolean defaultAddress; // Địa chỉ mặc định hay không, nếu true thì sẽ là địa chỉ mặc định của người dùng
}
