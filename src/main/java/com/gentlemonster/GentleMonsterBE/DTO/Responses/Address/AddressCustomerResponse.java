package com.gentlemonster.GentleMonsterBE.DTO.Responses.Address;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressCustomerResponse extends AddressResponse {
    private String name; // Tên người nhận địa chỉ
    private String phoneNumber; // Số điện thoại người nhận địa chỉ
    private boolean type; // Loại địa chỉ (ví dụ: "home", "office")
    private boolean isDefault; // Địa chỉ mặc định
}
