package com.gentlemonster.GentleMonsterBE.Services.Banner;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Banner.AddBannerRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Banner.BannerRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Banner.EditBannerRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Banner.BannerResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Banner.BaseBannerResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Banner.Public.BannerPublicResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public interface IBannerService {
    PagingResponse<List<BaseBannerResponse>> getAllBanners(@ModelAttribute BannerRequest bannerRequest);
    APIResponse<BannerResponse> getOneBanner(String bannerID);
    APIResponse<Boolean> addBanner(AddBannerRequest addBannerRequest);
    APIResponse<Boolean> updateBanner(String bannerID, EditBannerRequest editBannerRequest);
    APIResponse<Boolean> deleteBanner(String bannerID);
    APIResponse<List<BannerPublicResponse>> getAllBannersPublic();

}
