package com.gentlemonster.Controllers.Public;

import com.gentlemonster.Contants.Enpoint;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.Services.Banner.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class BannerPublicController {
    @Autowired
    private BannerService bannerService;

    @GetMapping(Enpoint.Banner.PUBLIC_BANNER)
    public ResponseEntity<APIResponse<?>> getAllBannerPublic(){
        return ResponseEntity.ok(bannerService.getAllBannersPublic());
    }
}
