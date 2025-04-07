package com.gentlemonster.GentleMonsterBE.DTO.Responses.Product;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseProductResponse {
    private UUID id;
    private String name;
    private String thumbnail;
    private boolean status;
}
