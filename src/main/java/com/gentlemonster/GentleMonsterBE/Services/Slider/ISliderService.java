package com.gentlemonster.GentleMonsterBE.Services.Slider;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Slider.AddSliderRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Slider.EditSliderRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Slider.SliderRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider.BaseSliderResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider.SliderPublicResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Slider.SliderResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ISliderService {
    PagingResponse<List<BaseSliderResponse>> getAllSlider(@ModelAttribute SliderRequest sliderRequest);
    APIResponse<Boolean> addSlider(AddSliderRequest addSliderRequest);
    APIResponse<Boolean> editSlider(String sliderID, EditSliderRequest editSliderRequest);
    APIResponse<Boolean> deleteSlider(String sliderID);
    APIResponse<SliderResponse> getOneSlider(String sliderID);
    APIResponse<List<SliderPublicResponse>> getAllSliderPublic(@PathVariable String categorySlug);
}
