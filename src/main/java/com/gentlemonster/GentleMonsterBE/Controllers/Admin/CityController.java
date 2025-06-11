package com.gentlemonster.GentleMonsterBE.Controllers.Admin;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.City.AddCityRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.City.CityRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.City.EditCityRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.Services.City.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(Enpoint.City.BASE)
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public ResponseEntity<PagingResponse<?>> getAllCities(@ModelAttribute CityRequest cityRequest) {
       return ResponseEntity.ok(cityService.getAllCities(cityRequest));
    }

    @PostMapping(Enpoint.City.NEW)
    public ResponseEntity<APIResponse<Boolean>> addCity(@RequestBody AddCityRequest addCityRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new APIResponse<>(false, errorMessages));
        }
        return ResponseEntity.ok(cityService.addCity(addCityRequest));
    }

    @PutMapping(Enpoint.City.EDIT)
    public ResponseEntity<APIResponse<Boolean>> editCity(@PathVariable String cityID, @RequestBody EditCityRequest editCityRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new APIResponse<>(false, errorMessages));
        }
        return ResponseEntity.ok(cityService.editCity(cityID, editCityRequest));
    }

    @DeleteMapping(Enpoint.City.DELETE)
    public ResponseEntity<APIResponse<Boolean>> deleteCity(@PathVariable String cityID) {
        return ResponseEntity.ok(cityService.deleteCity(cityID));
    }

    @PostMapping(Enpoint.City.MEDIA)
    public ResponseEntity<APIResponse<Boolean>> uploadMedia(@PathVariable String cityID, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(cityService.uploadMedia(cityID, file));
    }
}
