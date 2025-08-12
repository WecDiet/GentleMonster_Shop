package com.gentlemonster.DTO.Responses.Banner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gentlemonster.DTO.Responses.Category.CategoryResponse;
import com.gentlemonster.DTO.Responses.Cloudinary.ImageResponse;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerResponse {
    private UUID id;
    private String title;
    private boolean status;
    private int seq; // thứ tự banner từ 1 -> 4
    private int serial; // 0: image , 1: video
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime modifiedDate; // Ngày sửa
    private CategoryResponse category;
    private ImageResponse media; 
}
