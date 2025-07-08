package com.gentlemonster.GentleMonsterBE.DTO.Responses.Address;

import java.util.UUID;

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
public class AddressCustomerResponse {
    private String name; // Tên người nhận địa chỉ
    private String phoneNumber; // Số điện thoại người nhận địa chỉ
    private String street;
    private String ward;
    private String district;
    private String city;
    private String country;
    private boolean type; // Loại địa chỉ (ví dụ: "home", "office")
    private boolean isDefault; // Địa chỉ mặc định
    
}
