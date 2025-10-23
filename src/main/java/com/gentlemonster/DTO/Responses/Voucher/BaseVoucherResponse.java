package com.gentlemonster.DTO.Responses.Voucher;

import java.time.LocalDateTime;
import java.util.UUID;

import org.checkerframework.checker.units.qual.N;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseVoucherResponse {
    private UUID id;
    private String name;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime expirationDate;
    private boolean isGlobal;
    private boolean status; // true = active, false = inactive
}
