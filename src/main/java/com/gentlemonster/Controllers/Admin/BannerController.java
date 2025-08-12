package com.gentlemonster.Controllers.Admin;

import com.gentlemonster.Contants.Enpoint;
import com.gentlemonster.DTO.Requests.Banner.AddBannerRequest;
import com.gentlemonster.DTO.Requests.Banner.BannerRequest;
import com.gentlemonster.DTO.Requests.Banner.EditBannerRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.Banner.BannerResponse;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.Services.Banner.IBannerService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(Enpoint.Banner.BASE)
public class BannerController {

    @Autowired
    private IBannerService iBannerService;

    @GetMapping
    public ResponseEntity<PagingResponse<?>> getAllBanners(@ModelAttribute BannerRequest bannerRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new PagingResponse<>(null,  errorMessages, 0, (long) 0));
        }
        return ResponseEntity.ok(iBannerService.getAllBanners(bannerRequest));

    }

    @GetMapping(Enpoint.Banner.ID)
    public ResponseEntity<APIResponse<BannerResponse>> getBanner(@PathVariable String bannerID) throws NotFoundException {
        return ResponseEntity.ok(iBannerService.getOneBanner(bannerID));
    }

    @PostMapping(Enpoint.Banner.NEW)
    public ResponseEntity<APIResponse<Boolean>> addBanner(@Valid @ModelAttribute AddBannerRequest addBannerRequest, @RequestParam("image") MultipartFile image, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new APIResponse<>(null, errorMessages));
        }
        return ResponseEntity.ok(iBannerService.addBanner(addBannerRequest,image));
    }

    @PutMapping(Enpoint.Banner.EDIT)
    public ResponseEntity<APIResponse<Boolean>> updateBanner(@PathVariable String bannerID, @RequestBody EditBannerRequest editBannerRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new APIResponse<>(null, errorMessages));
        }
        return ResponseEntity.ok(iBannerService.updateBanner(bannerID, editBannerRequest));
    }

    @DeleteMapping(Enpoint.Banner.DELETE)
    public ResponseEntity<APIResponse<Boolean>> deleteBanner(@PathVariable String bannerID) throws NotFoundException {
        return ResponseEntity.ok(iBannerService.deleteBanner(bannerID));
    }

    @PostMapping(Enpoint.Banner.MEDIA)
    public ResponseEntity<APIResponse<Boolean>> uploadBannerMedia(@PathVariable String bannerID, @RequestParam("media") MultipartFile media) throws NotFoundException {
        return ResponseEntity.ok(iBannerService.uploadMediaBanner(bannerID, media));
    }
}
