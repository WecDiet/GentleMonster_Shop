package com.gentlemonster.GentleMonsterBE.DTO.Requests.Category;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {
    private int limit = -1;
    private int page = -1;
}
