package com.gentlemonster.GentleMonsterBE.Services.Category;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Category.AddCategoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Category.CategoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Category.EditCategoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Category.CategoryResponse;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
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
