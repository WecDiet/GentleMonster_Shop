package com.gentlemonster.GentleMonsterBE.Controllers.Public;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Store.StoreRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.Services.Store.StoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class StoresPublicController {
    @Autowired
    private StoreService storeService;

    @GetMapping("/us/store")
    public ResponseEntity<APIResponse<?>> getAllStorePublic(@Valid @ModelAttribute StoreRequest storeRequest,  BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new APIResponse<>(null,  errorMessages));
        }
        return ResponseEntity.ok(storeService.GetAllStoreByCountry(storeRequest));
    }
}
