package com.gentlemonster.GentleMonsterBE.DTO.Requests.Slider;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddSliderRequest {
    @NotEmpty(message = "Slider name is required")
    private String name;
    @NotEmpty(message = "Slider image is required")
    private String image;
    private boolean status;
    private boolean highlighted;
    private String category;
}
