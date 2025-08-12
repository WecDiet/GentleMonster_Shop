package com.gentlemonster.Services.ProductType;

import com.gentlemonster.Contants.MessageKey;
import com.gentlemonster.DTO.Requests.ProductType.AddProductTypeRequest;
import com.gentlemonster.DTO.Requests.ProductType.EditProductTypeRequest;
import com.gentlemonster.DTO.Requests.ProductType.ProductTypeRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.ProductType.BaseProductTypeResponse;
import com.gentlemonster.DTO.Responses.ProductType.ProductTypeResponse;
import com.gentlemonster.Entities.Category;
import com.gentlemonster.Entities.ProductType;
import com.gentlemonster.Entities.Slider;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Repositories.ICategoryRepository;
import com.gentlemonster.Repositories.IProductTypeRepository;
import com.gentlemonster.Repositories.ISliderRepository;
import com.gentlemonster.Repositories.Specification.ProductSpecification;
import com.gentlemonster.Utils.LocalizationUtils;
import com.gentlemonster.Utils.ValidationUtils;
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
import java.util.Arrays;
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
    private LocalizationUtils localizationUtil;
    @Autowired
    private ValidationUtils vietnameseStringUtils;
    @Autowired
    private ICategoryRepository iCategoryRepository;

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
    public APIResponse<Boolean> addProductType(AddProductTypeRequest addProductTypeRequest) throws NotFoundException {
        if(iProductTypeRepository.existsByName(addProductTypeRequest.getName())){
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_EXISTED)));
        }
        if (addProductTypeRequest.getSlider() == null) {
            // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
        }
        ProductType productType = modelMapper.map(addProductTypeRequest, ProductType.class);
        String slugStandardization = vietnameseStringUtils.removeAccents(addProductTypeRequest.getName()).toLowerCase().replaceAll("\\s+", "-").trim();
        productType.setSlug(slugStandardization);
        productType.setLinkURL("/"+slugStandardization);
        Slider slider = iSliderRepository.findByName(addProductTypeRequest.getSlider()).orElse(null);
        if (slider == null) {
            // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
        }
        Category category = iCategoryRepository.findByName(addProductTypeRequest.getCategory()).orElse(null);
        if (category == null) {
            // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND));
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
    public APIResponse<Boolean> editProductType(String productTypeID, EditProductTypeRequest editProductTypeRequest) throws NotFoundException {
        ProductType productType = iProductTypeRepository.findById(UUID.fromString(productTypeID)).orElse(null);
        if (productType == null) {
            // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND));
        }
        modelMapper.map(editProductTypeRequest, productType);
        String slugStandardization = vietnameseStringUtils.removeAccents(editProductTypeRequest.getName()).toLowerCase().replaceAll("\\s+", "-").trim();
        productType.setSlug(slugStandardization);
        productType.setLinkURL("/"+slugStandardization);
        Slider slider = iSliderRepository.findByName(editProductTypeRequest.getSlider()).orElse(null);
        if (slider == null) {
            // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
        }
        Category category = iCategoryRepository.findByName(editProductTypeRequest.getCategory()).orElse(null);
        if (category == null) {
            // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND));
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
    public APIResponse<Boolean> deleteProductType(String productTypeID) throws NotFoundException {
        ProductType productType = iProductTypeRepository.findById(UUID.fromString(productTypeID)).orElse(null);
        if (productType == null) {
            // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND));
        }
        iProductTypeRepository.delete(productType);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<ProductTypeResponse> getProductType(String productTypeID) throws NotFoundException {
        ProductType productType = iProductTypeRepository.findById(UUID.fromString(productTypeID)).orElse(null);
        if (productType == null) {
            // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND));
        }
        ProductTypeResponse productTypeResponse = modelMapper.map(productType, ProductTypeResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_GET_SUCCESS));
        return new APIResponse<>(productTypeResponse, messages);
    }


    // @Override
    // public APIResponse<List<ProductPublicResponse>> getAllProductTypePublic(String categorySlug, String sliderSlug) {
    //     List<ProductPublicResponse> productPublicResponseList;
    //     List<ProductType> productList;

    //     // Trường hợp đặc biệt: categorySlug = collaboration
    //     if ("collaboration".equalsIgnoreCase(categorySlug)) {
    //         List<String> targetCategories = Arrays.asList("sunglasses", "glasses");

    //         // Lấy các slider được đánh dấu highlighted = true và nằm trong 2 category trên
    //         List<Slider> collaborationSliders = iSliderRepository.findAll().stream()
    //                 .filter(slider -> slider.isHighlighted()
    //                         && slider.getCategory() != null
    //                         && targetCategories.contains(slider.getCategory().getSlug().toLowerCase()))
    //                 .toList();

    //         // Nếu là /collaboration/view-all → lấy toàn bộ productType của các slider hợp lệ
    //         if ("view-all".equalsIgnoreCase(sliderSlug)) {
    //             if (collaborationSliders.isEmpty()) {
    //                 return new APIResponse<>(new ArrayList<>(), List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_EMPTY)));
    //             }
    //             Specification<ProductType> specification = ProductSpecification.getListProductTypeBySliders(collaborationSliders);
    //             productList = iProductTypeRepository.findAll(specification);
    //         } else {
    //             // /collaboration/{sliderSlug} → tìm sliderSlug có trong collaborationSliders
    //             Slider slider = collaborationSliders.stream()
    //                     .filter(s -> s.getSlug().equalsIgnoreCase(sliderSlug))
    //                     .findFirst()
    //                     .orElse(null);

    //             if (slider == null) {
    //                 return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
    //             }

    //             Specification<ProductType> specification = ProductSpecification.getListProductTypeBySlider(sliderSlug);
    //             productList = iProductTypeRepository.findAll(specification);
    //         }

    //         productPublicResponseList = productList.stream()
    //                 .map(product -> modelMapper.map(product, ProductPublicResponse.class))
    //                 .toList();

    //         if (productPublicResponseList.isEmpty()) {
    //             return new APIResponse<>(productPublicResponseList, List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_EMPTY)));
    //         }

    //         return new APIResponse<>(productPublicResponseList, List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_GET_SUCCESS)));
    //     }

    //     // Trường hợp bình thường: kiểm tra category hợp lệ
    //     Category category = iCategoryRepository.findBySlug(categorySlug).orElse(null);
    //     if (category == null) {
    //         return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND)));
    //     }

    //     if ("view-all".equalsIgnoreCase(sliderSlug)) {
    //         Specification<ProductType> specification = ProductSpecification.getListProductTypeByCategorySlug(categorySlug);
    //         productList = iProductTypeRepository.findAll(specification);
    //     } else {
    //         Slider slider = iSliderRepository.findBySlug(sliderSlug).orElse(null);
    //         if (slider == null) {
    //             return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND)));
    //         }

    //         Specification<ProductType> specification = ProductSpecification.getListProductType(categorySlug, sliderSlug);
    //         productList = iProductTypeRepository.findAll(specification);
    //     }

    //     productPublicResponseList = productList.stream()
    //             .map(product -> modelMapper.map(product, ProductPublicResponse.class))
    //             .toList();

    //     if (productPublicResponseList.isEmpty()) {
    //         return new APIResponse<>(productPublicResponseList,
    //                 List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_EMPTY)));
    //     }

    //     return new APIResponse<>(productPublicResponseList,
    //             List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_GET_SUCCESS)));
    // }


}
