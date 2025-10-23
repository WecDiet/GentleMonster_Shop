package com.gentlemonster.DTO.Requests.Voucher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherRequest {
    private int limit = -1; // Giá trị mặc định là 10
    private int page = -1;   // Giá trị mặc định là 0
    private String code;
    private String name;
}