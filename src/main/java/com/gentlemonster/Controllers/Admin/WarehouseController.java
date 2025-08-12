package com.gentlemonster.Controllers.Admin;


import com.gentlemonster.Contants.Enpoint;
import com.gentlemonster.DTO.Requests.Warehouse.AddWarehouseRequest;
import com.gentlemonster.DTO.Requests.Warehouse.EditWarehouseRequest;
import com.gentlemonster.DTO.Requests.Warehouse.WarehouseRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.Warehouse.WarehouseResponse;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Services.Warehouse.WarehouseService;
import com.gentlemonster.Services.WarehouseProduct.WarehouseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Enpoint.Warehouse.BASE)
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;

    @GetMapping
    public ResponseEntity<PagingResponse<?>> getAllWarehouse(@ModelAttribute WarehouseRequest warehouseRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new PagingResponse<>(null,  errorMessages, 0, (long) 0));
        }
        return ResponseEntity.ok(warehouseService.getAllWarehouse(warehouseRequest));
    }

    @GetMapping(Enpoint.Warehouse.ID)
    public ResponseEntity<APIResponse<WarehouseResponse>> getWarehouseDetail(@PathVariable String warehouseID) throws NotFoundException {
        return ResponseEntity.ok(warehouseService.getWarehouseById(warehouseID));
    }

    @PostMapping(Enpoint.Warehouse.NEW)
    public ResponseEntity<APIResponse<Boolean>> createNewWarehouse(@RequestBody AddWarehouseRequest addWarehouseRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(warehouseService.addWarehouse(addWarehouseRequest));
    }

    @PutMapping(Enpoint.Warehouse.EDIT)
    public ResponseEntity<APIResponse<Boolean>> editWarehouse(@PathVariable String warehouseID, @RequestBody EditWarehouseRequest editWarehouseRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(warehouseService.updateWarehouse(warehouseID, editWarehouseRequest));
    }

    @DeleteMapping(Enpoint.Warehouse.DELETE)
    public ResponseEntity<APIResponse<Boolean>> deleteWarehouse(@PathVariable String warehouseID) throws NotFoundException {
        return ResponseEntity.ok(warehouseService.deleteWarehouse(warehouseID));
    }

}
