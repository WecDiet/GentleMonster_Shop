package com.gentlemonster.GentleMonsterBE.DTO.Requests.ProductType;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditProductTypeRequest {
    @NotEmpty(message = "Name Product type is required")
    private String name;
    private String description;
    private boolean status;
    private String slider;
}
