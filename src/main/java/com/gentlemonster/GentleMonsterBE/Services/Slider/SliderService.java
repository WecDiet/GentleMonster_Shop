package com.gentlemonster.GentleMonsterBE.Services.Slider;

import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Slider.AddSliderRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Slider.EditSliderRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Slider.SliderRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider.BaseSliderResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider.SliderPublicResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider.SliderResponse;
import com.gentlemonster.GentleMonsterBE.Entities.Category;
import com.gentlemonster.GentleMonsterBE.Entities.Collaboration;
import com.gentlemonster.GentleMonsterBE.Entities.Media;
import com.gentlemonster.GentleMonsterBE.Entities.Slider;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;
import com.gentlemonster.GentleMonsterBE.Repositories.ICategoryRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.ICollaborationRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.ISliderRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.Specification.SliderSpecification;
import com.gentlemonster.GentleMonsterBE.Services.Cloudinary.CloudinaryService;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtils;
import com.gentlemonster.GentleMonsterBE.Utils.ValidationUtils;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@NoArgsConstructor
public class SliderService implements ISliderService {

    @Autowired
    private ISliderRepository iSliderRepository;
    @Autowired
    private ICategoryRepository iCategoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LocalizationUtils localizationUtil;
    @Autowired
    private ValidationUtils vietnameseStringUtils;
    @Autowired
    private ICollaborationRepository ICollaborationRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public PagingResponse<List<BaseSliderResponse>> getAllSlider(@ModelAttribute SliderRequest sliderRequest) {
        List<BaseSliderResponse> sliderResponseList;
        List<Slider> sliderList;
        Pageable pageable;
        if(sliderRequest.getPage() == 0 && sliderRequest.getLimit() == 0){
            sliderList = iSliderRepository.findAll();
            sliderResponseList = sliderList.stream()
                    .map(slider -> modelMapper.map(slider, BaseSliderResponse.class))
                    .toList();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_GET_SUCCESS));
            return new PagingResponse<>(sliderResponseList, messages, 1, (long) sliderList.size());
        }else {
            sliderRequest.setPage(Math.max(sliderRequest.getPage(), 1));
            pageable = PageRequest.of(sliderRequest.getPage() - 1, sliderRequest.getLimit(), Sort.by("createdDate").descending());
        }
        Page<Slider> sliderPage = iSliderRepository.findAll(pageable);
        sliderList = sliderPage.getContent();
        sliderResponseList = sliderList.stream()
                .map(slider -> modelMapper.map(slider, BaseSliderResponse.class))
                .toList();
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_GET_SUCCESS));
        return new PagingResponse<>(sliderResponseList, messages, sliderPage.getTotalPages(), sliderPage.getTotalElements());
    }

    @Override
    public APIResponse<Boolean> addSlider(AddSliderRequest addSliderRequest) throws NotFoundException {
        if (iSliderRepository.existsByName(addSliderRequest.getName())) {
            return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.SLIDER_EXISTED)));
        }
        if (addSliderRequest.getCategory() == null){
            return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_REQUIRED)));
        }
        Slider slider = modelMapper.map(addSliderRequest, Slider.class);
        String slugStandardization = vietnameseStringUtils.removeAccents(addSliderRequest.getName()).toLowerCase().replaceAll("\\s+", "-").trim();
        slider.setSlug(slugStandardization);

        Category category = iCategoryRepository.findByName(addSliderRequest.getCategory()).orElse(null);
        if (category == null){
            // return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND));
        }
        slider.setCategory(category);
        iSliderRepository.save(slider);

        if(addSliderRequest.isHighlighted()){
             Collaboration collaboration = Collaboration.builder()
                     .slider(slider)
                     .status(true)
                     .build();
            ICollaborationRepository.save(collaboration);
        }
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_CREATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> editSlider(String sliderID, EditSliderRequest editSliderRequest) throws NotFoundException {
        Slider slider = iSliderRepository.findById(UUID.fromString(sliderID)).orElse(null);
        if (slider == null){
            // return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
        }

        modelMapper.map(editSliderRequest, slider);
        Category category = iCategoryRepository.findByName(editSliderRequest.getCategory()).orElse(null);
        if (category == null){
            // return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND));
        }
        slider.setCategory(category);
        String slugStandardization = vietnameseStringUtils.removeAccents(editSliderRequest.getName()).toLowerCase().replaceAll("\\s+", "-").trim();
        slider.setSlug(slugStandardization);
        slider.setModifiedDate(LocalDateTime.now());
        // Cập nhật trạng thái highlighted
        boolean previousHighlighted = slider.isHighlighted(); // Trạng thái trước khi chỉnh sửa
        boolean newHighlighted = editSliderRequest.isHighlighted(); // Trạng thái mới
        if(iSliderRepository.existsByHighlighted(true) && newHighlighted){
            return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.SLIDER_HIGHLIGHTED_EXISTED)));
        }
        slider.setHighlighted(newHighlighted);
        iSliderRepository.save(slider);

        if(newHighlighted && previousHighlighted){
            Collaboration collaboration = Collaboration.builder()
                    .slider(slider)
                    .status(true)
                    .build();
            ICollaborationRepository.save(collaboration);
        }else if(!newHighlighted && !previousHighlighted){
            ICollaborationRepository.deleteBySlider(slider);
        }
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_UPDATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> deleteSlider(String sliderID) throws NotFoundException {
        Slider slider = iSliderRepository.findById(UUID.fromString(sliderID)).orElse(null);
        if (slider == null){
            // return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
        }
        // Media sliderThumb = slider.getThumbnailMedia();
        // if (sliderThumb != null && sliderThumb.getPublicId() != null) {
        //     cloudinaryService.deleteMedia(sliderThumb.getPublicId());
        // }

        if (slider.getImage() != null && slider.getImage().getPublicId() != null) {
            cloudinaryService.deleteMedia(slider.getImage().getPublicId());
        }
        iSliderRepository.delete(slider);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<SliderResponse> getOneSlider(String sliderID) throws NotFoundException {
        Slider slider = iSliderRepository.findById(UUID.fromString(sliderID)).orElse(null);
        if (slider == null){
            // return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
        }
        SliderResponse sliderResponse = modelMapper.map(slider, SliderResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_GET_SUCCESS));
        return new APIResponse<>(sliderResponse, messages);
    }


    @Override
    public APIResponse<List<SliderPublicResponse>> getAllSliderPublic(@PathVariable String categorySlug) throws NotFoundException {
        List<SliderPublicResponse> sliderPublicResponseList;
        List<Slider> sliderList;
        Category categoryRepo = iCategoryRepository.findBySlug(categorySlug).orElse(null);
        Specification<Slider> sliderPublic = SliderSpecification.getListSlider(categoryRepo.getSlug());
        if (sliderPublic == null){
            // return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
        }
        sliderList = iSliderRepository.findAll(sliderPublic);
        sliderPublicResponseList = sliderList.stream()
                .map(slider -> modelMapper.map(slider, SliderPublicResponse.class))
                .toList();
        if (sliderPublicResponseList.isEmpty()) {
            return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.SLIDER_EMPTY)));
        }
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_GET_SUCCESS));
        return new APIResponse<>(sliderPublicResponseList, messages);
    }


    // @Override
    // public void uploadImage(String sliderID, MultipartFile image, String type) {
    //     try {
    //         if (type.equalsIgnoreCase("THUMBNAIL")) {
    //             handleUploadThumbnail(sliderID, image);
    //         } else if (type.equalsIgnoreCase("IMAGE")) {
    //             handleUploadImage(sliderID, image);
    //         } else {
    //             throw new IllegalArgumentException("Invalid image type: " + type);
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         throw new RuntimeException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_UPLOAD_MEDIA_FAILED));
    //     }
    // }


    // private APIResponse<Boolean> handleUploadThumbnail(String sliderID, MultipartFile thumbnail) throws NotFoundException {
    //     Slider slider = iSliderRepository.findById(UUID.fromString(sliderID)).orElse(null);
    //     if (slider == null) {
    //         // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
    //         throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
    //     }
    //     try {
    //         if(slider.getThumbnailMedia() != null){
    //             // Xóa media cũ nếu có
    //             cloudinaryService.deleteMedia(slider.getThumbnailMedia().getPublicId());
    //             slider.setThumbnailMedia(new Media());
    //         }

    //         Map uploadThumbnailResult = cloudinaryService.uploadMedia(thumbnail, "sliders");
    //         String thumbnailUrl = (String) uploadThumbnailResult.get("secure_url");
    //         Media thumbnailMedia = Media.builder()
    //             .imageUrl(thumbnailUrl)
    //             .publicId((String) uploadThumbnailResult.get("public_id"))
    //             .referenceId(slider.getId())
    //             .referenceType("SLIDER")
    //             .altText("Slider Thumbnail: " + slider.getName())
    //             .type("THUMBNAIL")
    //             .build();
    //         slider.setThumbnailMedia(thumbnailMedia);

    //         iSliderRepository.save(slider);
    //         List<String> messages = new ArrayList<>();
    //         messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_UPLOAD_MEDIA_SUCCESS));
    //         return new APIResponse<>(true, messages);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         List<String> messages = new ArrayList<>();
    //         messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_UPLOAD_MEDIA_FAILED));
    //         return new APIResponse<>(false, messages);
    //     }

    // }
    @Override
    public APIResponse<Boolean> handleUploadImage(String sliderID, MultipartFile image) throws NotFoundException {
        Slider slider = iSliderRepository.findById(UUID.fromString(sliderID)).orElse(null);
        if (slider == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
        }
        try {
            if (slider.getImage() != null) {
                // Xóa media cũ nếu có
                cloudinaryService.deleteMedia(slider.getImage().getPublicId());
                slider.setImage(new Media());
            }
            Map uploadResult = cloudinaryService.uploadMedia(image, "sliders");
            String imageUrl = (String) uploadResult.get("secure_url");
            Media sliderMedia = Media.builder()
                    .imageUrl(imageUrl)
                    .publicId((String) uploadResult.get("public_id"))
                    .referenceId(slider.getId())
                    .referenceType("SLIDER")
                    .altText("Slider Image: " + slider.getName())
                    .type("IMAGE")
                    .build();
            slider.setImage(sliderMedia);
            iSliderRepository.save(slider);
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_UPLOAD_MEDIA_SUCCESS));
            return new APIResponse<>(true, messages);
        } catch (Exception e) {
            e.printStackTrace();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_UPLOAD_MEDIA_FAILED));
            return new APIResponse<>(false, messages);
        }
    }
}
