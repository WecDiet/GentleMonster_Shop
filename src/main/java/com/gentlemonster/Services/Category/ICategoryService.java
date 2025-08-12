package com.gentlemonster.Services.Category;

import com.gentlemonster.DTO.Requests.Category.AddCategoryRequest;
import com.gentlemonster.DTO.Requests.Category.CategoryRequest;
import com.gentlemonster.DTO.Requests.Category.EditCategoryRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.Category.CategoryResponse;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.DTO.Responses.PagingResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ICategoryService {
    PagingResponse<List<CategoryResponse>> getAllCategory(@ModelAttribute CategoryRequest categoryRequest);
    APIResponse<Boolean> addCategory(@RequestBody AddCategoryRequest addCategoryRequest);
    APIResponse<Boolean> editCategory(@PathVariable String categoryID, @RequestBody EditCategoryRequest editCategoryRequest) throws NotFoundException;
    APIResponse<Boolean> deleteCategory(@PathVariable String categoryID) throws NotFoundException;
}
