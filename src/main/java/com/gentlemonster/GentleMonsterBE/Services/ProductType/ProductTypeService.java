package com.gentlemonster.GentleMonsterBE.Services.ProductType;

import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.ProductType.AddProductTypeRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.ProductType.EditProductTypeRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.ProductType.ProductTypeRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.ProductType.BaseProductTypeResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.ProductType.ProductTypeResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.ProductType.Public.ProductPublicResponse;
import com.gentlemonster.GentleMonsterBE.Entities.Category;
import com.gentlemonster.GentleMonsterBE.Entities.ProductType;
import com.gentlemonster.GentleMonsterBE.Entities.Slider;
import com.gentlemonster.GentleMonsterBE.Repositories.ICategoryRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IProductTypeRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.ISliderRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.Specification.ProductTypeSpecification;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtil;
import com.gentlemonster.GentleMonsterBE.Utils.VietnameseStringUtils;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@NoArgsConstructor
public class ProductTypeService implements IProductTypeService {

    @Autowired
    private IProductTypeRepository iProductTypeRepository;
    @Autowired
    private ISliderRepository iSliderRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LocalizationUtil localizationUtil;
    @Autowired
    private VietnameseStringUtils vietnameseStringUtils;
    @Autowired
    private ICategoryRepository iCategoryRepository;

//    @Override
//    public PagingResponse<List<BaseProductTypeResponse>> getAllProductType(ProductTypeRequest productTypeRequest) {
//        List<BaseProductTypeResponse> productTypeResponseList;
//        List<ProductType> productTypeList;
//        Pageable pageable;
//
//        int page = Math.max(productTypeRequest.getPage() - 1, 0); // Page index should start from 0
//        int size = productTypeRequest.getLimit() > 0 ? productTypeRequest.getLimit() : 10; // Default size is 10 if limit is not provided
//        pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
//
//        Page<ProductType> productTypes = iProductTypeRepository.findAll(pageable);
//        productTypeList = productTypes.getContent();
//        productTypeResponseList = productTypeList.stream()
//                .map(productType -> modelMapper.map(productType, BaseProductTypeResponse.class))
//                .toList();
//        if (productTypeResponseList.isEmpty()) {
//            return new PagingResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_EMPTY)), 0, 0L);
//        }
//        List<String> messages = new ArrayList<>();
//        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_GET_SUCCESS));
//        return new PagingResponse<>(productTypeResponseList, messages, productTypes.getTotalPages(), productTypes.getTotalElements());
//    }

    @Override
    public PagingResponse<List<BaseProductTypeResponse>> getAllProductType(ProductTypeRequest productTypeRequest) {
        List<BaseProductTypeResponse> productTypeResponseList;
        List<ProductType> productTypeList;
        Pageable pageable;
        if(productTypeRequest.getPage() == 0 && productTypeRequest.getLimit() == 0) {
            productTypeList = iProductTypeRepository.findAll();
            productTypeResponseList = productTypeList.stream()
                    .map(productType -> modelMapper.map(productType, BaseProductTypeResponse.class))
                    .toList();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_GET_SUCCESS));
            return new PagingResponse<>(productTypeResponseList, messages, 1, (long) productTypeResponseList.size());
        }else {
            productTypeRequest.setPage(Math.max(productTypeRequest.getPage(), 1));
            pageable = PageRequest.of(productTypeRequest.getPage() - 1, productTypeRequest.getLimit());
        }
        Page<ProductType> productTypes = iProductTypeRepository.findAll(pageable);
        productTypeList = productTypes.getContent();
        productTypeResponseList = productTypeList.stream()
                .map(productType -> modelMapper.map(productType, BaseProductTypeResponse.class))
                .toList();
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_GET_SUCCESS));
        return new PagingResponse<>(productTypeResponseList, messages, productTypes.getTotalPages(), productTypes.getTotalElements());
    }

    @Override
    public APIResponse<Boolean> addProductType(AddProductTypeRequest addProductTypeRequest) {
        if(iProductTypeRepository.existsByName(addProductTypeRequest.getName())){
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_EXISTED)));
        }
        if (addProductTypeRequest.getSlider() == null) {
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
        }
        ProductType productType = modelMapper.map(addProductTypeRequest, ProductType.class);
        String slugStandardization = vietnameseStringUtils.removeAccents(addProductTypeRequest.getName()).toLowerCase().replaceAll("\\s+", "-").trim();
        productType.setSlug(slugStandardization);
        productType.setLinkURL("/"+slugStandardization);
        Slider slider = iSliderRepository.findByName(addProductTypeRequest.getSlider()).orElse(null);
        if (slider == null) {
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
        }
        Category category = iCategoryRepository.findByName(addProductTypeRequest.getCategory()).orElse(null);
        if (category == null) {
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND)));
        }
        if(!slider.getCategory().equals(category)){
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_IN_CATEGORY)));
        }
        productType.setCategory(category);
        productType.setSlider(slider);
        iProductTypeRepository.save(productType);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_CREATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> editProductType(String productTypeID, EditProductTypeRequest editProductTypeRequest) {
        ProductType productType = iProductTypeRepository.findById(UUID.fromString(productTypeID)).orElse(null);
        if (productType == null) {
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND)));
        }
        modelMapper.map(editProductTypeRequest, productType);
        String slugStandardization = vietnameseStringUtils.removeAccents(editProductTypeRequest.getName()).toLowerCase().replaceAll("\\s+", "-").trim();
        productType.setSlug(slugStandardization);
        productType.setLinkURL("/"+slugStandardization);
        Slider slider = iSliderRepository.findByName(editProductTypeRequest.getSlider()).orElse(null);
        if (slider == null) {
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
        }
        Category category = iCategoryRepository.findByName(editProductTypeRequest.getCategory()).orElse(null);
        if (category == null) {
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND)));
        }
        productType.setCategory(category);
        productType.setSlider(slider);
        productType.setModifiedDate(LocalDateTime.now());
        iProductTypeRepository.save(productType);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_UPDATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> deleteProductType(String productTypeID) {
        ProductType productType = iProductTypeRepository.findById(UUID.fromString(productTypeID)).orElse(null);
        if (productType == null) {
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND)));
        }
        iProductTypeRepository.delete(productType);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<ProductTypeResponse> getProductType(String productTypeID) {
        ProductType productType = iProductTypeRepository.findById(UUID.fromString(productTypeID)).orElse(null);
        if (productType == null) {
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND)));
        }
        ProductTypeResponse productTypeResponse = modelMapper.map(productType, ProductTypeResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_GET_SUCCESS));
        return new APIResponse<>(productTypeResponse, messages);
    }

    @Override
    public APIResponse<List<ProductPublicResponse>> getAllProductTypePublic(String categorySlug,String sliderSlug) {
        List<ProductPublicResponse> productPublicResponseList;
        List<ProductType> productList;
        Category category = iCategoryRepository.findBySlug(categorySlug).orElse(null);
        if (category == null){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND));
            return new APIResponse<>(null, messages);
        }
        if ("view-all".equals(sliderSlug) && category.getSlug().equals(categorySlug)){
            Specification<ProductType> specification = ProductTypeSpecification.getListProductTypeByCategorySlug(categorySlug);
            productList = iProductTypeRepository.findAll(specification);
            productPublicResponseList = productList.stream()
                    .map(product -> modelMapper.map(product, ProductPublicResponse.class))
                    .toList();
            if (productPublicResponseList.isEmpty()){
                List<String> messages = new ArrayList<>();
                messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_EMPTY));
                return new APIResponse<>(productPublicResponseList, messages);
            }
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_GET_SUCCESS));
            return new APIResponse<>(productPublicResponseList, messages);
        }
        Slider slider = iSliderRepository.findBySlug(sliderSlug).orElse(null);
        if (slider == null){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
            return new APIResponse<>(null, messages);
        }
        Specification<ProductType> specification = ProductTypeSpecification.getListProductType(categorySlug, sliderSlug);
        productList = iProductTypeRepository.findAll(specification);
        productPublicResponseList = productList.stream()
                .map(product -> modelMapper.map(product, ProductPublicResponse.class))
                .toList();
        if (productPublicResponseList.isEmpty()){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_EMPTY));
            return new APIResponse<>(productPublicResponseList, messages);
        }
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_GET_SUCCESS));
        return new APIResponse<>(productPublicResponseList, messages);
    }
}
