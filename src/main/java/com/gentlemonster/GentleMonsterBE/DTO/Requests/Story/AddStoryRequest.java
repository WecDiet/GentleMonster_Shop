package com.gentlemonster.GentleMonsterBE.DTO.Requests.Story;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddStoryRequest {
    //@NotEmpty(message = "Name cannot be empty !")
    private String name;
    //@NotEmpty(message = "Description cannot be empty !")
    private String description;
    private boolean status;
    //@NotEmpty(message = "Product Type cannot be empty !")
    private String productType;
    // @NotEmpty(message = "Media story cannot be empty !")
    private List<String> mediaStory;
    //@NotEmpty(message = "Collaboration name cannot be empty !")
    private String collaboration;
}
