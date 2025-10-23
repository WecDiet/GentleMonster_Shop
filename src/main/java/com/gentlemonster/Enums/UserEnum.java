package com.gentlemonster.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UserEnum {
    ADMIN("ADMIN", "Administrator"),
    COMPANY_EMPLOYEE("SUBSIDIARY", "Employee"),
    COMPANY_MANAGER("SUBSIDIARY", "Manager"),
    WAREHOUSE_EMPLOYEE("WAREHOUSE", "Employee"),
    WAREHOUSE_MANAGER("WAREHOUSE", "Manager"),
    CUSTOMER("CUSTOMER", "Client");

    private String role;
    private String position;

}

