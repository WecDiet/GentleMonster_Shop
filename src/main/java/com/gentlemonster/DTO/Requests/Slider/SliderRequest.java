package com.gentlemonster.DTO.Requests.Slider;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SliderRequest {
    @NotBlank(message = "Limit is required")
    private int limit = -1; // Giá trị mặc định là 10
    @NotBlank(message = "Page is required")
    private int page = -1;   // Giá trị mặc định là 0
    private String name; // Không thể null
}
