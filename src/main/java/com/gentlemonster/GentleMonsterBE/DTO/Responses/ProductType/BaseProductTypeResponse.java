package com.gentlemonster.GentleMonsterBE.DTO.Responses.ProductType;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseProductTypeResponse {
    private String id;
    private String name;
    private String slug;
    private boolean status;
    private String linkURL;
}
