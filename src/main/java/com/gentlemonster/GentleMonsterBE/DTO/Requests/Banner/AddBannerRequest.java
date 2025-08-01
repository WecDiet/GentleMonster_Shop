package com.gentlemonster.GentleMonsterBE.DTO.Requests.Banner;

import lombok.*;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddBannerRequest {
    private String title;
    private boolean status;
    // private String image;
    private int seq;
    // private int serial;
    private String category;
    private String slider;
}
