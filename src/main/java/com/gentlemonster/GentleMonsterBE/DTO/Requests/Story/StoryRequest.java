package com.gentlemonster.GentleMonsterBE.DTO.Requests.Story;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoryRequest {
	private int limit = -1; // Default value is -1
    private int page = -1;  // Default value is -1
}
