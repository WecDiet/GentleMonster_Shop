package com.gentlemonster.GentleMonsterBE.DTO.Responses.Store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StorePublicResponse {
    private String storeName;
    private String street;
    private String ward;
    private String district;
    private String hotLine;
    private String email;
    private String description;
}
