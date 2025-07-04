package com.gentlemonster.GentleMonsterBE.Controllers.Admin;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Store.AddStoreRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Store.StoreRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;
import com.gentlemonster.GentleMonsterBE.Services.Store.StoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(Enpoint.Store.BASE)
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping
    public ResponseEntity<PagingResponse<?>> getAllStore(@ModelAttribute StoreRequest storeRequest) {
        System.out.println(Enpoint.Store.STORE_PUBLIC);
        System.out.println(Enpoint.API_PREFIX_SHOP + Enpoint.Store.STORE_PUBLIC);
        return ResponseEntity.ok(storeService.getAllStore(storeRequest));
    }

    @GetMapping(Enpoint.Store.ID)
    public ResponseEntity<APIResponse<?>> getStoreById(@PathVariable String storeID) throws NotFoundException {
        return ResponseEntity.ok(storeService.getOneStore(storeID));
    }

    @PostMapping(Enpoint.Store.NEW)
    public ResponseEntity<APIResponse<Boolean>> addStore(@Valid @RequestBody AddStoreRequest addStoreRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(storeService.addStore(addStoreRequest));
    }

    @DeleteMapping(Enpoint.Store.DELETE)
    public ResponseEntity<APIResponse<Boolean>> deleteStore(@PathVariable String storeID) throws NotFoundException {
        return ResponseEntity.ok(storeService.deleteStore(storeID));
    }

    @PostMapping(Enpoint.Store.MEDIA)
    public ResponseEntity<APIResponse<Boolean>> uploadMedia(@PathVariable String storeID, 
                                                @RequestParam("image") MultipartFile[] images) throws NotFoundException {
        return ResponseEntity.ok(storeService.handleUploadGallery(storeID, images));
    }
}
