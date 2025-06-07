package com.gentlemonster.GentleMonsterBE.DTO.Responses.Story;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseStoryResponse {
    private UUID id;
    private String name;
    private boolean status;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime createdDate;
}
