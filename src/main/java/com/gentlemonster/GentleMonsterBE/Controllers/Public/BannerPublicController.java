package com.gentlemonster.GentleMonsterBE.Controllers.Public;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.Services.Banner.BannerService;
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
