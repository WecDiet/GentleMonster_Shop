package com.gentlemonster.DTO.Requests.Banner;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerRequest {
    private int page = -1;
    private int limit = -1;
    @NotEmpty(message = "Title Banner is required")
    private String title;
}
