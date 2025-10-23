package com.gentlemonster.DTO.Requests.Voucher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditVoucherRequest {
    private String name;
    private String code;
    private String description;
    private String discountType;
    private BigDecimal discount;
    private BigDecimal maxDiscount;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime expirationDate;
    private boolean isGlobal;
    private boolean status;
    private List<String> productCode;
}
