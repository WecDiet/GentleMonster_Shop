package com.gentlemonster.DTO.Requests.Category;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditCategoryRequest {
    @NotEmpty(message = "Category name is required")
    private String name;
    private String description;
    private boolean status;
}
