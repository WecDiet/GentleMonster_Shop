package com.gentlemonster.Controllers.Admin;

import com.gentlemonster.Contants.Enpoint;
import com.gentlemonster.DTO.Requests.Category.AddCategoryRequest;
import com.gentlemonster.DTO.Requests.Category.CategoryRequest;
import com.gentlemonster.DTO.Requests.Category.EditCategoryRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Services.Category.CategoryService;
import com.gentlemonster.Services.Slider.SliderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Enpoint.Category.BASE)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SliderService sliderService;


    @GetMapping
    public ResponseEntity<PagingResponse<?>> getAllCategory(@ModelAttribute CategoryRequest categoryRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new PagingResponse<>(null,  errorMessages, 0, (long) 0));
        }
        return ResponseEntity.ok(categoryService.getAllCategory(categoryRequest));
    }

    @PostMapping(Enpoint.Category.NEW)
    public ResponseEntity<APIResponse<Boolean>> addCategory(@RequestBody AddCategoryRequest addCategoryRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new APIResponse<>(false,  errorMessages));
        }
        return ResponseEntity.ok(categoryService.addCategory(addCategoryRequest));
    }

    @PutMapping(Enpoint.Category.EDIT)
    public ResponseEntity<APIResponse<Boolean>> editCategory(@PathVariable String categoryID,@RequestBody EditCategoryRequest editCategoryRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new APIResponse<>(false,  errorMessages));
        }
        return ResponseEntity.ok(categoryService.editCategory(categoryID,editCategoryRequest));
    }

    @DeleteMapping(Enpoint.Category.DELETE)
    public ResponseEntity<APIResponse<Boolean>> deleteCategory(@PathVariable String categoryID) throws NotFoundException {
        return ResponseEntity.ok(categoryService.deleteCategory(categoryID));
    }
}
