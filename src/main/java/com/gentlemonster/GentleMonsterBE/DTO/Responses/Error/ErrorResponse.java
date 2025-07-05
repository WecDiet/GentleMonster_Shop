package com.gentlemonster.GentleMonsterBE.DTO.Responses.Error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private Integer errCode;
    private String errMessage;
}
