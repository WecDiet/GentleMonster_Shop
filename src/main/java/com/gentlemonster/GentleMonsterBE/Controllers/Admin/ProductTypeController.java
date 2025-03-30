package com.gentlemonster.GentleMonsterBE.Controllers.Admin;


import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.ProductType.AddProductTypeRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.ProductType.EditProductTypeRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.ProductType.ProductTypeRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.Services.ProductType.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Enpoint.Product_Type.BASE)
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    @GetMapping
    public ResponseEntity<PagingResponse<?>> getAllProductType(@ModelAttribute ProductTypeRequest productTypeRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new PagingResponse<>(null,  errorMessages, 0, (long) 0));
        }
        return ResponseEntity.ok(productTypeService.getAllProductType(productTypeRequest));
    }

    @GetMapping(Enpoint.Product_Type.ID)
    public ResponseEntity<APIResponse<?>> getProductTypeById(@PathVariable String productTypeID) {
        return ResponseEntity.ok(productTypeService.getProductType(productTypeID));
    }

    @PostMapping(Enpoint.Product_Type.NEW)
    public ResponseEntity<APIResponse<Boolean>> addProductType(@RequestBody AddProductTypeRequest addProductTypeRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new APIResponse<>(false,  errorMessages));
        }
        return ResponseEntity.ok(productTypeService.addProductType(addProductTypeRequest));
    }

    @PutMapping(Enpoint.Product_Type.EDIT)
    public ResponseEntity<APIResponse<Boolean>> editProductType(@PathVariable String productTypeID, @RequestBody EditProductTypeRequest editProductTypeRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new APIResponse<>(false,  errorMessages));
        }
        return ResponseEntity.ok(productTypeService.editProductType(productTypeID,editProductTypeRequest));
    }

    @DeleteMapping(Enpoint.Product_Type.DELETE)
    public ResponseEntity<APIResponse<Boolean>> deleteProductType(@PathVariable String productTypeID) {
        return ResponseEntity.ok(productTypeService.deleteProductType(productTypeID));
    }
}
