package com.gentlemonster.Services.Banner;

import com.gentlemonster.Contants.MessageKey;
import com.gentlemonster.Entities.Slider;
import com.gentlemonster.DTO.Requests.Banner.AddBannerRequest;
import com.gentlemonster.DTO.Requests.Banner.BannerRequest;
import com.gentlemonster.DTO.Requests.Banner.EditBannerRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.Banner.BannerResponse;
import com.gentlemonster.DTO.Responses.Banner.BaseBannerResponse;
import com.gentlemonster.DTO.Responses.Banner.Public.BannerPublicResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.Entities.Banner;
import com.gentlemonster.Entities.Category;
import com.gentlemonster.Entities.Media;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Repositories.IBannerRepository;
import com.gentlemonster.Repositories.ICategoryRepository;
import com.gentlemonster.Repositories.ISliderRepository;
import com.gentlemonster.Services.Cloudinary.CloudinaryService;
import com.gentlemonster.Utils.LocalizationUtils;
import com.gentlemonster.Utils.ValidationUtils;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@NoArgsConstructor
public class BannerService implements IBannerService {
    @Autowired
    private IBannerRepository iBannerRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LocalizationUtils localizationUtil;
    @Autowired
    private ValidationUtils vietnameseStringUtils;
    @Autowired
    private ICategoryRepository iCategoryRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ISliderRepository iSliderRepository;

    @Override
    public PagingResponse<List<BaseBannerResponse>> getAllBanners(BannerRequest bannerRequest) {
        List<BaseBannerResponse> bannerResponsesList;
        List<Banner> bannerList;
        Pageable pageable;
        if (bannerRequest.getPage() == 0 && bannerRequest.getLimit() == 0) {
            bannerList = iBannerRepository.findAll();
            bannerResponsesList = bannerList.stream()
                    .map(banner -> modelMapper.map(banner, BaseBannerResponse.class))
                    .toList();
            List<String> messages = List.of(localizationUtil.getLocalizedMessage(MessageKey.BANNER_GET_SUCCESS));
            return new PagingResponse<>(bannerResponsesList, messages, 1, (long) bannerResponsesList.size());
        } else {
            bannerRequest.setPage(Math.max(bannerRequest.getPage(), 1));
            pageable = PageRequest.of(bannerRequest.getPage() - 1, bannerRequest.getLimit(), Sort.by("createdDate").descending());
        }
        Page<Banner> bannerPage = iBannerRepository.findAll(pageable);
        bannerList =  bannerPage.getContent();
        bannerResponsesList = bannerList.stream()
                .map(banner1 -> modelMapper.map(banner1, BaseBannerResponse.class))
                .toList();

        if (bannerResponsesList.isEmpty()){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.BANNER_EMPTY));
            return new PagingResponse<>(null, messages, 0, (long) 0);
        }
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.BANNER_GET_SUCCESS));
        return new PagingResponse<>(bannerResponsesList, messages, bannerPage.getTotalPages(), bannerPage.getTotalElements());
    }

    @Override
    public APIResponse<BannerResponse> getOneBanner(String bannerID) throws NotFoundException {
        Banner banner = iBannerRepository.findById(UUID.fromString(bannerID)).orElse(null);
        if(banner == null) {
            // List<String> messages = List.of(localizationUtil.getLocalizedMessage(MessageKey.BANNER_NOT_FOUND));
            // return new APIResponse<>(null, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.BANNER_NOT_FOUND));
        }
        BannerResponse bannerResponse = modelMapper.map(banner, BannerResponse.class);
        List<String> messages = List.of(localizationUtil.getLocalizedMessage(MessageKey.BANNER_GET_SUCCESS));
        return new APIResponse<>(bannerResponse, messages);
    }

    @Override
    public APIResponse<Boolean> addBanner(AddBannerRequest addBannerRequest, MultipartFile image) throws NotFoundException {
        if(iBannerRepository.existsByTitle(addBannerRequest.getTitle())){
            List<String> messages = List.of(localizationUtil.getLocalizedMessage(MessageKey.BANNER_EXISTED));
            return new APIResponse<>(false, messages);
        }
        Banner banner = modelMapper.map(addBannerRequest, Banner.class);
        banner.setSeq(addBannerRequest.getSeq());
        String categoryStandardization = vietnameseStringUtils.removeAccents(addBannerRequest.getCategory()).toLowerCase().replaceAll("\\s+", "-").trim();
        String sliderStandardization = vietnameseStringUtils.removeAccents(addBannerRequest.getSlider()).toLowerCase().replaceAll("\\s+", "-").trim();
        Slider slider = iSliderRepository.findByName(addBannerRequest.getSlider()).orElse(null);
        if (slider == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
        }
        banner.setSlider(slider);
        banner.setLink("/" + categoryStandardization + "/" + sliderStandardization);
        Category category = iCategoryRepository.findByName(addBannerRequest.getCategory()).orElse(null);
        if (category == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND));
        }
        banner.setCategory(category);
        if (image != null && !image.isEmpty()) {
            try {
                Map uploadResult = cloudinaryService.uploadMedia(image, "banners");
                String imageURL = (String) uploadResult.get("secure_url");
                Media mediaBanner = Media.builder()
                        .imageUrl(imageURL)
                        .publicId((String) uploadResult.get("public_id"))
                        .referenceId(banner.getId())
                        .referenceType("BANNER")
                        .altText("Banner photo: " + banner.getTitle())
                        .type("MEDIA")
                        .build();
                banner.setImage(mediaBanner);
            } catch (Exception e) {
                e.printStackTrace();
                List<String> messages = List.of(localizationUtil.getLocalizedMessage(MessageKey.BANNER_UPLOAD_MEDIA_FAILED));
                return new APIResponse<>(false, messages);
            }
            
        }

        iBannerRepository.save(banner);
        List<String> messages = List.of(localizationUtil.getLocalizedMessage(MessageKey.BANNER_CREATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> updateBanner(String bannerID, EditBannerRequest editBannerRequest)throws NotFoundException {
        Banner banner = iBannerRepository.findById(UUID.fromString(bannerID)).orElse(null);
        if(banner == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.BANNER_NOT_FOUND));
        }
        modelMapper.map(editBannerRequest, banner);
        banner.setSeq(editBannerRequest.getSeq());
        String categoryStandardization = vietnameseStringUtils.removeAccents(editBannerRequest.getCategory()).toLowerCase().replaceAll("\\s+", "-").trim();
        String sliderStandardization = vietnameseStringUtils.removeAccents(editBannerRequest.getSlider()).toLowerCase().replaceAll("\\s+", "-").trim();
        banner.setLink("/" + categoryStandardization + "/" + sliderStandardization);
        Category category = iCategoryRepository.findByName(editBannerRequest.getCategory()).orElse(null);
        if (category == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND));
        }
        banner.setCategory(category);
        iBannerRepository.save(banner);
        List<String> messages = List.of(localizationUtil.getLocalizedMessage(MessageKey.BANNER_UPDATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> deleteBanner(String bannerID) throws NotFoundException{
        Banner banner = iBannerRepository.findById(UUID.fromString(bannerID)).orElse(null);
        if(banner == null) {
            // List<String> messages = List.of(localizationUtil.getLocalizedMessage(MessageKey.BANNER_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.BANNER_NOT_FOUND));
        }
        Media bannerMedia = banner.getImage();
        if (bannerMedia != null && bannerMedia.getPublicId() != null) {
            // Delete media from cloudinary if it exists
            cloudinaryService.deleteMedia(bannerMedia.getPublicId());
        }
        iBannerRepository.delete(banner);
        List<String> messages = List.of(localizationUtil.getLocalizedMessage(MessageKey.BANNER_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<List<BannerPublicResponse>> getAllBannersPublic() {
        int limit = 4;
        List<BannerPublicResponse> bannerPublicResponseList;
        List<Banner> bannerList;
        Pageable pageable;

        pageable = PageRequest.of(0, limit, Sort.by("seq").descending());
        Page<Banner> bannerPage = iBannerRepository.findAll(pageable);
        bannerList =  bannerPage.getContent();
        bannerPublicResponseList = bannerList.stream()
                .map(banner -> modelMapper.map(banner , BannerPublicResponse.class))
                .toList();
        if (bannerPublicResponseList.isEmpty()){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.BANNER_EMPTY));
            return new APIResponse<>(null, messages);
        }
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.BANNER_GET_SUCCESS));
        return new APIResponse<>(bannerPublicResponseList, messages);
    }

    @Override
    public APIResponse<Boolean> uploadMediaBanner(String bannerID, MultipartFile media) throws NotFoundException {
        Banner banner = iBannerRepository.findById(UUID.fromString(bannerID)).orElse(null);
        if (banner == null) {
            // List<String> messages = List.of(localizationUtil.getLocalizedMessage(MessageKey.BANNER_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.BANNER_NOT_FOUND));
        }
        try {
            if (banner.getImage() != null) {
                // If the banner already has media, delete it first
                cloudinaryService.deleteMedia(banner.getImage().getPublicId());
                banner.setImage(new Media());
            }
            Map uploadResult = cloudinaryService.uploadMedia(media, "banners");
            String imageURL = (String) uploadResult.get("secure_url");
            Media mediaEntity = Media.builder()
                    .imageUrl(imageURL)
                    .publicId((String) uploadResult.get("public_id"))
                    .referenceId(banner.getId())
                    .referenceType("BANNER")
                    .altText("Banner photo: " + banner.getTitle())
                    .type("MEDIA")
                    .build();
            banner.setImage(mediaEntity);
            iBannerRepository.save(banner);
            List<String> messages = List.of(localizationUtil.getLocalizedMessage(MessageKey.BANNER_UPLOAD_MEDIA_SUCCESS));
            return new APIResponse<>(true, messages);
        } catch (Exception e) {
            e.printStackTrace();
            List<String> messages = List.of(localizationUtil.getLocalizedMessage(MessageKey.BANNER_UPLOAD_MEDIA_FAILED));
            return new APIResponse<>(false, messages);
        }
    }


}
