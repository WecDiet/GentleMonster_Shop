package com.gentlemonster.GentleMonsterBE.DTO.Requests.Collaboration;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollaborationRequest {
    private int page = -1;
    private int limit = -1;
    private String name;
}
