package com.gentlemonster.GentleMonsterBE.DTO.Requests.Store;

import lombok .*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditStoreRequest {
    private String storeName;
    private String street;
    private String ward;
    private String district;
    private String hotLine;
    private String email;
    private String description;
    private boolean status;
    private String city;
    private List<String> productTypes;
    private String slider;
}
