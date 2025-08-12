package com.gentlemonster.Controllers.Admin;

import com.gentlemonster.Contants.Enpoint;
import com.gentlemonster.DTO.Requests.Product.AddProductRequest;
import com.gentlemonster.DTO.Requests.Product.EditProductRequest;
import com.gentlemonster.DTO.Requests.Product.ProductRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Services.Product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(Enpoint.Product.BASE)
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<PagingResponse<?>> getAllProduct(@ModelAttribute ProductRequest productRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new PagingResponse<>(null,  errorMessages, 0, (long) 0));
        }
        return ResponseEntity.ok(productService.getAllProduct(productRequest));
    }

    @PostMapping(Enpoint.Product.NEW)
    public ResponseEntity<APIResponse<Boolean>> createNewProduct(@Valid @RequestBody AddProductRequest addProductRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(productService.addProduct(addProductRequest));
    }

    @PutMapping(Enpoint.Product.EDIT)
    public ResponseEntity<APIResponse<Boolean>> editProduct(@PathVariable String productID,@Valid @RequestBody EditProductRequest editProductRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(productService.editProduct(productID,editProductRequest));
    }

    @PostMapping(Enpoint.Product.UPLOAD_IMAGE)
    public ResponseEntity<APIResponse<Boolean>> uploadProductImage(@PathVariable String productID, 
                                                @RequestParam("image") MultipartFile[] images) throws NotFoundException {
        return ResponseEntity.ok(productService.handleUploadImages(productID, images));
    }



    @DeleteMapping(Enpoint.Product.DELETE)
    public ResponseEntity<APIResponse<Boolean>> deleteProduct(@PathVariable String productID) throws NotFoundException {
        return ResponseEntity.ok(productService.deleteProduct(productID));
    }

    @GetMapping(Enpoint.Product.ID)
    public ResponseEntity<APIResponse<?>> getProductDetail(@PathVariable String productID) throws NotFoundException {
        return ResponseEntity.ok(productService.getOneProduct(productID));
    }
}
