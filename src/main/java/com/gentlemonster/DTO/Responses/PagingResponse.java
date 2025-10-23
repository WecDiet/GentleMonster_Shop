package com.gentlemonster.DTO.Responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse<T> {
    private T data;
    private List<String> message;
    private int totalPage;
    private Long totalCount;
    private int totalNew;

    public PagingResponse(T data, List<String> message, int totalPage, Long totalCount) {
        this.data = data;
        this.message = message;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
    }
}
