package com.gentlemonster.Services.Slider;

import com.gentlemonster.DTO.Requests.Slider.AddSliderRequest;
import com.gentlemonster.DTO.Requests.Slider.EditSliderRequest;
import com.gentlemonster.DTO.Requests.Slider.SliderRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.Slider.BaseSliderResponse;
import com.gentlemonster.DTO.Responses.Slider.SliderPublicResponse;
import com.gentlemonster.DTO.Responses.Slider.SliderResponse;
import com.gentlemonster.Exception.NotFoundException;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISliderService {
    PagingResponse<List<BaseSliderResponse>> getAllSlider(@ModelAttribute SliderRequest sliderRequest);
    APIResponse<Boolean> addSlider(AddSliderRequest addSliderRequest) throws NotFoundException;
    APIResponse<Boolean> editSlider(String sliderID, EditSliderRequest editSliderRequest) throws NotFoundException;
    APIResponse<Boolean> deleteSlider(String sliderID) throws NotFoundException;
    APIResponse<SliderResponse> getOneSlider(String sliderID) throws NotFoundException;
    APIResponse<List<SliderPublicResponse>> getAllSliderPublic(@PathVariable String categorySlug) throws NotFoundException;
    APIResponse<Boolean> handleUploadImage(String sliderID, MultipartFile image) throws NotFoundException;
}
