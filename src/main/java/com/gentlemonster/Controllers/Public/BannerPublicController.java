package com.gentlemonster.Controllers.Public;

import com.gentlemonster.Contants.Enpoint;
import com.gentlemonster.Contants.MessageKey;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.Services.Banner.BannerService;
import com.gentlemonster.Services.RateLimit.RateLimitService;
import com.gentlemonster.Utils.LocalizationUtils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class BannerPublicController {
    @Autowired
    private BannerService bannerService;
    @Autowired
    private RateLimitService rateLimitService;
    @Autowired
    private LocalizationUtils localizationUtil;

    @GetMapping(Enpoint.Banner.PUBLIC_BANNER)
    public ResponseEntity<APIResponse<?>> getAllBannerPublic(HttpServletRequest request) {
        String action = "GET:" + Enpoint.Banner.PUBLIC_BANNER;
        String type = "public";
        // if(!rateLimitService.isAllowed(request, action, type)) {
        //     return ResponseEntity.status(429).body(new APIResponse<>(429, "Too Many Requests", null));
        // }
        // return ResponseEntity.ok(bannerService.getAllBannersPublic());
        try {
            rateLimitService.isAllowed(request, action, type);
            return ResponseEntity.ok(bannerService.getAllBannersPublic());
        } catch (Exception e) {
            List<String> messages = List.of(localizationUtil.getLocalizedMessage(MessageKey.RATE_LIMIT_EXCEEDED));
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new APIResponse<>(429, messages));
        }
    }
}
