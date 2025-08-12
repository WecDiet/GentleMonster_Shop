package com.gentlemonster.Controllers.Public;

import com.gentlemonster.Contants.Enpoint;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.Slider.SliderPublicResponse;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Services.Slider.ISliderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Enpoint.API_PREFIX_SHOP)
public class SliderPublicController {
    @Autowired
    private ISliderService sliderService;

    @GetMapping(Enpoint.Slider.PUBLIC_SLIDER)
    public APIResponse<List<SliderPublicResponse>> getAllSliderPublic(@PathVariable String slug) throws NotFoundException{
        if (slug == null) {
            return new APIResponse<>(null, List.of("Category is required !!!!"));
        }
        return sliderService.getAllSliderPublic(slug);
    }
}
