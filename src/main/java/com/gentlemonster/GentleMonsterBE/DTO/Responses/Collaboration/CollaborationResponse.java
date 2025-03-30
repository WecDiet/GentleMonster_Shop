package com.gentlemonster.GentleMonsterBE.DTO.Responses.Collaboration;


import com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider.BaseSliderResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollaborationResponse {
    private UUID id;
    private BaseSliderResponse slider;
    private String createdDate;
    private String modifiedDate;
    private boolean status;

}
