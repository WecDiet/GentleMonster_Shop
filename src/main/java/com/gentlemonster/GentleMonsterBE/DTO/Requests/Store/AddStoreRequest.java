package com.gentlemonster.GentleMonsterBE.DTO.Requests.Store;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddStoreRequest {
    private String storeName;
    private String street;
    private String ward;
    private String district;
    private String hotLine;
    private String email;
    private String description;
    private boolean status;
    private String city;
//    private String slider;
}
