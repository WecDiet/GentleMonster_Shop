package com.gentlemonster.GentleMonsterBE.Controllers.Admin;


import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.WarehouseProduct.AddProductWarehouseRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.WarehouseProduct.EditProductWarehouseRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.WarehouseProduct.WarehouseProductRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;
import com.gentlemonster.GentleMonsterBE.Services.WarehouseProduct.WarehouseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(Enpoint.Warehouse.BASE)
public class ProductWarehouseController {
    @Autowired
    private WarehouseProductService warehouseProductService;

    @GetMapping(Enpoint.Warehouse.GET_WAREHOUSE)
    public ResponseEntity<PagingResponse<?>> getAllProductInWarehouse(@ModelAttribute WarehouseProductRequest warehouseProductRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new PagingResponse<>(null,  errorMessages, 0, (long) 0));
        }
        System.out.println(Enpoint.Warehouse.BASE);
        return ResponseEntity.ok(warehouseProductService.getAllProductInWarehouse(warehouseProductRequest));
    }

    @PostMapping(Enpoint.Warehouse.ADD_PRODUCT)
    public ResponseEntity<APIResponse<Boolean>> addProductToWarehouse(@RequestBody AddProductWarehouseRequest addProductToWarehouseRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(warehouseProductService.addProductToWarehouse(addProductToWarehouseRequest));
    }

    @PutMapping(Enpoint.Warehouse.EDIT_PRODUCT)
    public ResponseEntity<APIResponse<Boolean>> editProductInWarehouse(@PathVariable String productWarehouseID, @RequestBody EditProductWarehouseRequest editProductWarehouseRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(warehouseProductService.editProductInWarehouse(productWarehouseID, editProductWarehouseRequest));
    }

    @DeleteMapping(Enpoint.Warehouse.DELETE_PRODUCT)
    public ResponseEntity<APIResponse<Boolean>> deleteProductInWarehouse(@PathVariable String productWarehouseID) throws NotFoundException {
        return ResponseEntity.ok(warehouseProductService.deleteProductInWarehouse(productWarehouseID));
    }

    @GetMapping(Enpoint.Warehouse.ID_PRODUCT)
    public ResponseEntity<APIResponse<?>> getProductInWarehouse(@PathVariable String productWarehouseID) throws NotFoundException {
        return ResponseEntity.ok(warehouseProductService.getProductInWarehouse(productWarehouseID));
    }

    @PostMapping(Enpoint.Warehouse.UPLOAD_MEDIA)
    public ResponseEntity<APIResponse<Boolean>> uploadMediaProductInWarehouse(@PathVariable String productWarehouseID, @RequestParam("file") MultipartFile file) throws NotFoundException {
        return ResponseEntity.ok(warehouseProductService.uploadMediaProductInWarehouse(productWarehouseID, file));
    }
}
