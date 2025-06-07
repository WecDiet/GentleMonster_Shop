package com.gentlemonster.GentleMonsterBE.DTO.Responses.Story.Public;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseStoryPublicResponse {
    private String name;
    private List<String> mediaStory;
}
