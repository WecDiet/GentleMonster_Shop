package com.gentlemonster.GentleMonsterBE.Services.Slider;

import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Category.AddCategoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Slider.AddSliderRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Slider.EditSliderRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Slider.SliderRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider.BaseSliderResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider.SliderPublicResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider.SliderResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.User.BaseUserResponse;
import com.gentlemonster.GentleMonsterBE.Entities.Category;
import com.gentlemonster.GentleMonsterBE.Entities.Collaboration;
import com.gentlemonster.GentleMonsterBE.Entities.Slider;
import com.gentlemonster.GentleMonsterBE.Entities.User;
import com.gentlemonster.GentleMonsterBE.Repositories.ICategoryRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.ICollaborationRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.ISliderRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.Specification.SliderSpecification;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtil;
import com.gentlemonster.GentleMonsterBE.Utils.VietnameseStringUtils;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private LocalizationUtil localizationUtil;
    @Autowired
    private VietnameseStringUtils vietnameseStringUtils;
    @Autowired
    private ICollaborationRepository ICollaborationRepository;

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
    public APIResponse<Boolean> addSlider(AddSliderRequest addSliderRequest) {
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
            return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND)));
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
    public APIResponse<Boolean> editSlider(String sliderID, EditSliderRequest editSliderRequest) {
        Slider slider = iSliderRepository.findById(UUID.fromString(sliderID)).orElse(null);
        if (slider == null){
            return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
        }

        modelMapper.map(editSliderRequest, slider);
        Category category = iCategoryRepository.findByName(editSliderRequest.getCategory()).orElse(null);
        if (category == null){
            return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND)));
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
    public APIResponse<Boolean> deleteSlider(String sliderID) {
        Slider slider = iSliderRepository.findById(UUID.fromString(sliderID)).orElse(null);
        if (slider == null){
            return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
        }
        iSliderRepository.delete(slider);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<SliderResponse> getOneSlider(String sliderID) {
        Slider slider = iSliderRepository.findById(UUID.fromString(sliderID)).orElse(null);
        if (slider == null){
            return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
        }
        SliderResponse sliderResponse = modelMapper.map(slider, SliderResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_GET_SUCCESS));
        return new APIResponse<>(sliderResponse, messages);
    }


    @Override
    public APIResponse<List<SliderPublicResponse>> getAllSliderPublic(@PathVariable String categorySlug) {
        List<SliderPublicResponse> sliderPublicResponseList;
        List<Slider> sliderList;
        Category categoryRepo = iCategoryRepository.findBySlug(categorySlug).orElse(null);
        Specification<Slider> sliderPublic = SliderSpecification.getListSlider(categoryRepo.getSlug());
        if (sliderPublic == null){
            return new APIResponse<>(null,List.of( localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
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
}
