package com.gentlemonster.GentleMonsterBE.Services.Banner;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Banner.AddBannerRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Banner.BannerRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Banner.EditBannerRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Banner.BannerResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Banner.BaseBannerResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Banner.Public.BannerPublicResponse;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;

import org.aspectj.weaver.ast.Not;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBannerService {
    PagingResponse<List<BaseBannerResponse>> getAllBanners(@ModelAttribute BannerRequest bannerRequest);
    APIResponse<BannerResponse> getOneBanner(String bannerID) throws NotFoundException;
    APIResponse<Boolean> addBanner(AddBannerRequest addBannerRequest,MultipartFile media) throws NotFoundException;
    APIResponse<Boolean> updateBanner(String bannerID, EditBannerRequest editBannerRequest) throws NotFoundException;
    APIResponse<Boolean> deleteBanner(String bannerID) throws NotFoundException;
    APIResponse<List<BannerPublicResponse>> getAllBannersPublic();
    APIResponse<Boolean> uploadMediaBanner(String bannerID, MultipartFile media) throws NotFoundException;

}
