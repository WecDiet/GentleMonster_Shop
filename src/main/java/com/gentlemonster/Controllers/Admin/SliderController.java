package com.gentlemonster.Controllers.Admin;

import com.gentlemonster.Contants.Enpoint;
import com.gentlemonster.DTO.Requests.Slider.AddSliderRequest;
import com.gentlemonster.DTO.Requests.Slider.EditSliderRequest;
import com.gentlemonster.DTO.Requests.Slider.SliderRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Services.Slider.SliderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<APIResponse<?>> getOneSlider(@PathVariable String sliderID) throws NotFoundException {
        return ResponseEntity.ok(sliderService.getOneSlider(sliderID));
    }

    @PostMapping(Enpoint.Slider.NEW)
    public ResponseEntity<APIResponse<Boolean>> createNewSlider(@Valid @RequestBody AddSliderRequest addSliderRequest, BindingResult result) throws NotFoundException {
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
    public ResponseEntity<APIResponse<Boolean>> editSlider(@PathVariable String sliderID, @Valid @RequestBody EditSliderRequest editSliderRequest, BindingResult result) throws NotFoundException {
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
    public ResponseEntity<APIResponse<Boolean>> deleteSlider(@PathVariable String sliderID) throws NotFoundException {
        return ResponseEntity.ok(sliderService.deleteSlider(sliderID));
    }

    @PostMapping(Enpoint.Slider.MEDIA)
    public ResponseEntity<?> uploadSliderImage(@PathVariable String sliderID, @RequestParam("image") MultipartFile image) throws NotFoundException {
        sliderService.handleUploadImage(sliderID, image);
        return ResponseEntity.status(200).body(new APIResponse<>(null, List.of("Image uploaded successfully")));
    }
}
