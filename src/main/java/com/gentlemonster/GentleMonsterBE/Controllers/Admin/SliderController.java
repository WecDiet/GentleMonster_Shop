package com.gentlemonster.GentleMonsterBE.Controllers.Admin;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Slider.AddSliderRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Slider.EditSliderRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Slider.SliderRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.Services.Slider.SliderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Enpoint.Slider.BASE)
public class SliderController {

    @Autowired
    private SliderService sliderService;

    @GetMapping
    public ResponseEntity<PagingResponse<?>> getAllSlider(@ModelAttribute SliderRequest sliderRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new PagingResponse<>(null,  errorMessages, 0, (long) 0));
        }
        return ResponseEntity.ok(sliderService.getAllSlider(sliderRequest));
    }

    @GetMapping(Enpoint.Slider.ID)
    public ResponseEntity<APIResponse<?>> getOneSlider(@PathVariable String sliderID) {
        return ResponseEntity.ok(sliderService.getOneSlider(sliderID));
    }

    @PostMapping(Enpoint.Slider.NEW)
    public ResponseEntity<APIResponse<Boolean>> createNewSlider(@Valid @RequestBody AddSliderRequest addSliderRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(sliderService.addSlider(addSliderRequest));
    }

    @PutMapping(Enpoint.Slider.EDIT)
    public ResponseEntity<APIResponse<Boolean>> editSlider(@PathVariable String sliderID, @Valid @RequestBody EditSliderRequest editSliderRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(sliderService.editSlider(sliderID, editSliderRequest));
    }

    @DeleteMapping(Enpoint.Slider.DELETE)
    public ResponseEntity<APIResponse<Boolean>> deleteSlider(@PathVariable String sliderID) {
        return ResponseEntity.ok(sliderService.deleteSlider(sliderID));
    }
}
