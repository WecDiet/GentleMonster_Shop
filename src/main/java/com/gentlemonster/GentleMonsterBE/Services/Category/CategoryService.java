package com.gentlemonster.GentleMonsterBE.Services.Category;

import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Category.AddCategoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Category.CategoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Category.EditCategoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Category.CategoryResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.Entities.Category;
import com.gentlemonster.GentleMonsterBE.Repositories.ICategoryRepository;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtil;
import com.gentlemonster.GentleMonsterBE.Utils.VietnameseStringUtils;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@NoArgsConstructor
public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository iCategoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LocalizationUtil localizationUtil;
    @Autowired
    private VietnameseStringUtils vietnameseStringUtils;


//    @Override
//    public PagingResponse<List<CategoryResponse>> getAllCategory(CategoryRequest categoryRequest) {
//        List<CategoryResponse> categoryResponseList;
//        List<Category> categoryList;
//        Pageable pageable;
//        int page = Math.max(categoryRequest.getPage() - 1, 0); // Page index should start from 0
//        int size = categoryRequest.getLimit() > 0 ? categoryRequest.getLimit() : 10; // Default size is 10 if limit is not provided
//        pageable = PageRequest.of(page, size, Sort.by("name").descending());
//        Page<Category> categoryPage = iCategoryRepository.findAll(pageable);
//        categoryList = categoryPage.getContent();
//        categoryResponseList = categoryList.stream()
//                .map(category -> modelMapper.map(category, CategoryResponse.class))
//                .toList();
//        List<String> messages = new ArrayList<>();
//        messages.add(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_GET_SUCCESS));
//        return new PagingResponse<>(categoryResponseList, messages,  categoryPage.getTotalPages(), categoryPage.getTotalElements());
//    }

    @Override
    public PagingResponse<List<CategoryResponse>> getAllCategory(CategoryRequest categoryRequest) {
        List<CategoryResponse> categoryResponseList;
        List<Category> categoryList;
        Pageable pageable;
        if (categoryRequest.getPage() == 0 && categoryRequest.getLimit() == 0) {
            categoryList = iCategoryRepository.findAll();
            categoryResponseList = categoryList.stream()
                    .map(category -> modelMapper.map(category, CategoryResponse.class))
                    .toList();
            List<String> messages = List.of(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_GET_SUCCESS));
            return new PagingResponse<>(categoryResponseList, messages, 1, (long) categoryResponseList.size());
        } else {
            categoryRequest.setPage(Math.max(categoryRequest.getPage(), 1));
            pageable = PageRequest.of(categoryRequest.getPage() - 1, categoryRequest.getLimit());
        }

        Page<Category> categoryPage = iCategoryRepository.findAll(pageable);
        categoryList = categoryPage.getContent();
        categoryResponseList = categoryList.stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .toList();
        List<String> message = new ArrayList<>();
        message.add(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_GET_SUCCESS));
        return new PagingResponse<>(categoryResponseList, message, categoryPage.getTotalPages(), categoryPage.getTotalElements());
    }

    @Override
    public APIResponse<Boolean> addCategory(AddCategoryRequest addCategoryRequest) {
        Category category = modelMapper.map(addCategoryRequest, Category.class);
        if(addCategoryRequest.getName() == null || addCategoryRequest.getName().isEmpty()){
            return new APIResponse<>(null, List.of("Category name is required"));
        }
        if (iCategoryRepository.existsByName(addCategoryRequest.getName())){
            return new APIResponse<>(null, List.of("Category name is already exists"));
        }

        category.setName(addCategoryRequest.getName());
        String slugStandardization = vietnameseStringUtils.removeAccents(addCategoryRequest.getName()).toLowerCase().trim();
        category.setSlug(slugStandardization);
        category.setLinkURL("/"+slugStandardization);
        iCategoryRepository.save(category);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_CREATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> editCategory(@PathVariable String categoryID , @RequestBody EditCategoryRequest editCategoryRequest) {
        Category category = iCategoryRepository.findById(UUID.fromString(categoryID)).orElse(null);
        if(category == null){
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND)));
        }
        if(editCategoryRequest.getName() == null || editCategoryRequest.getName().isEmpty()){
            return new APIResponse<>(null, List.of("Category name is required"));
        }
        
        modelMapper.map(editCategoryRequest, category);
        String slugStandardization = vietnameseStringUtils.removeAccents(editCategoryRequest.getName()).toLowerCase().trim();
        category.setSlug(slugStandardization);
        category.setLinkURL("/"+slugStandardization);
        iCategoryRepository.save(category);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_UPDATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> deleteCategory(String categoryID) {
        Category category = iCategoryRepository.findById(UUID.fromString(categoryID)).orElse(null);
        if(category == null){
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND)));
        }
        iCategoryRepository.delete(category);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }
}
