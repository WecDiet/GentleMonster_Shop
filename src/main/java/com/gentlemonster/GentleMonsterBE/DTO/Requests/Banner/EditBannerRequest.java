package com.gentlemonster.GentleMonsterBE.DTO.Requests.Banner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditBannerRequest {
    private String title;
    private String thumbnail;
    private boolean status;
    private String image;
    private int seq;
    private int serial;
    private String category;
    private String slider;
}
