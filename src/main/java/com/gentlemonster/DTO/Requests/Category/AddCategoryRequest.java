package com.gentlemonster.DTO.Requests.Category;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryRequest {
    private String name;
    private String description;
    private boolean status;
}
