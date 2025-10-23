package com.gentlemonster.DTO.Responses.Voucher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gentlemonster.DTO.Responses.Product.BaseProductResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherResponse {
    private UUID id;
    private String name;
    private String code;
    private String description;
    private String discountType;
    private BigDecimal discount;
    private BigDecimal maxDiscount;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime expirationDate;
    private boolean isGlobal;
    private boolean status; // true = active, false = inactive
    private List<BaseProductResponse> products;
}
