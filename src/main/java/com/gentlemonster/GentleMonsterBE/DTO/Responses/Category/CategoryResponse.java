package com.gentlemonster.GentleMonsterBE.DTO.Responses.Category;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private UUID id;
    private String name;
    private String description;
    private String linkURL;
    private String slug;
    private boolean status;
}
