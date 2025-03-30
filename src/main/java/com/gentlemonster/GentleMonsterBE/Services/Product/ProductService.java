package com.gentlemonster.GentleMonsterBE.Services.Product;

import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Product.AddProductRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Product.EditProductRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Product.ProductRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.BaseProductResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.ProductResponse;
import com.gentlemonster.GentleMonsterBE.Entities.*;
import com.gentlemonster.GentleMonsterBE.Repositories.IProductDetailRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IProductRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IProductTypeRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IWarehouseProductRepository;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtil;
import com.gentlemonster.GentleMonsterBE.Utils.VietnameseStringUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository iProductRepository;
    @Autowired
    private IProductDetailRepository iProductDetailRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LocalizationUtil localizationUtil;
    @Autowired
    private VietnameseStringUtils vietnameseStringUtils;
    @Autowired
    private IProductTypeRepository iProductTypeRepository;
    @Autowired
    private IWarehouseProductRepository iWarehouseProductRepository;
    @Autowired
    private IProductDetailRepository productDetailRepository;

    @Override
    public PagingResponse<List<BaseProductResponse>> getAllProduct(ProductRequest productRequest) {
        List<BaseProductResponse> baseProductListResponses;
        List<Product> productList;
        Pageable pageable;
        if (productRequest.getPage() == 0 && productRequest.getLimit() == 0){
            productList = iProductRepository.findAll();
            baseProductListResponses = productList.stream()
                    .map(product -> modelMapper.map(product, BaseProductResponse.class))
                    .toList();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_GET_SUCCESS));
            return new PagingResponse<>(baseProductListResponses, messages,1, (long) baseProductListResponses.size());
        }else {
            productRequest.setPage(Math.max(productRequest.getPage(), 1));
            pageable = PageRequest.of(productRequest.getPage() - 1, productRequest.getLimit());
        }
        Page<Product> productPage = iProductRepository.findAll(pageable);
        productList = productPage.getContent();
        baseProductListResponses = productList.stream()
                .map(product -> modelMapper.map(product, BaseProductResponse.class))
                .toList();
        if (baseProductListResponses.isEmpty()){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_EMPTY));
            return new PagingResponse<>(baseProductListResponses, messages, 1,0L);
        }
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_GET_SUCCESS));
        return new PagingResponse<>(baseProductListResponses, messages, productPage.getTotalPages(), productPage.getTotalElements());
    }
 
    @Override
    public APIResponse<ProductResponse> getOneProduct(String productID) {
        Product product = iProductRepository.findById(UUID.fromString(productID)).orElse(null);
        if(product == null){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_NOT_FOUND));
            return new APIResponse<>(null, messages);
        }
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_GET_SUCCESS));
        return new APIResponse<>(productResponse, messages);
    }

    @Override
    public APIResponse<Boolean> addProduct(AddProductRequest addProductRequest) {
        if (!iWarehouseProductRepository.existsByProductName(addProductRequest.getProductName())){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        if (iWarehouseProductRepository.existsByPublicProductFalse()){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_WAREHOUSE_DONT_PUBLIC));
            return new APIResponse<>(false, messages);
        }
        Product product = modelMapper.map(addProductRequest, Product.class);
        product.setName(addProductRequest.getProductName());
        String slugStandardization = vietnameseStringUtils.removeAccents(addProductRequest.getProductName()).trim().toLowerCase().replaceAll("\\s+", "-");
        product.setSlug(slugStandardization);
//        product.setWarehouseActive(addProductRequest.isWarehouseActive());
        ProductDetail productDetail = ProductDetail.builder()
                .frame(addProductRequest.getFrame())
                .lens(addProductRequest.getLens())
                .shape(addProductRequest.getShape())
                .material(addProductRequest.getMaterial())
                .lens_Width(addProductRequest.getLens_Width())
                .lens_Height(addProductRequest.getLens_Height())
                .bridge(addProductRequest.getBridge())
                .country(addProductRequest.getCountry())
                .manufacturer(addProductRequest.getManufacturer())
                .product(product)
                .build();
        product.setProductDetail(productDetail);
        ProductType productType = iProductTypeRepository.findByName(addProductRequest.getProductType()).orElse(null);
        if (productType == null){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        product.setProductType(productType);
        iProductRepository.save(product);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_CREATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> editProduct(String productID, EditProductRequest editProductRequest) {
        // Tìm sản phẩm theo ID
        Product productEdit = iProductRepository.findById(UUID.fromString(productID)).orElse(null);
        if (productEdit == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        if (iWarehouseProductRepository.existsByPublicProductFalse()){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_WAREHOUSE_DONT_PUBLIC));
            return new APIResponse<>(false, messages);
        }
        // Cập nhật các trường của Product
        productEdit.setName(editProductRequest.getProductName());
        String slugStandardization = vietnameseStringUtils.removeAccents(editProductRequest.getProductName())
                .toLowerCase().replaceAll("\\s+", "-").trim();
        productEdit.setSlug(slugStandardization);

        // Xử lý ProductDetail
        ProductDetail productDetail;
        if (productEdit.getProductDetail() != null) {
            // Nếu ProductDetail đã tồn tại, cập nhật các trường của nó
            productDetail = productEdit.getProductDetail();
            productDetail.setFrame(editProductRequest.getFrame());
            productDetail.setLens(editProductRequest.getLens());
            productDetail.setShape(editProductRequest.getShape());
            productDetail.setMaterial(editProductRequest.getMaterial());
            productDetail.setLens_Width(editProductRequest.getLens_Width());
            productDetail.setLens_Height(editProductRequest.getLens_Height());
            productDetail.setBridge(editProductRequest.getBridge());
            productDetail.setCountry(editProductRequest.getCountry());
            productDetail.setManufacturer(editProductRequest.getManufacturer());
        } else {
            // Nếu ProductDetail chưa tồn tại, tạo mới
            productDetail = ProductDetail.builder()
                    .frame(editProductRequest.getFrame())
                    .lens(editProductRequest.getLens())
                    .shape(editProductRequest.getShape())
                    .material(editProductRequest.getMaterial())
                    .lens_Width(editProductRequest.getLens_Width())
                    .lens_Height(editProductRequest.getLens_Height())
                    .bridge(editProductRequest.getBridge())
                    .country(editProductRequest.getCountry())
                    .manufacturer(editProductRequest.getManufacturer())
                    .product(productEdit)
                    .build();
            productEdit.setProductDetail(productDetail);
        }

        // Cập nhật ProductType
        ProductType productType = iProductTypeRepository.findByName(editProductRequest.getProductType()).orElse(null);
        if (productType == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        productEdit.setProductType(productType);

        // Lưu sản phẩm (sẽ tự động lưu hoặc cập nhật ProductDetail nhờ CascadeType.ALL)
        iProductRepository.save(productEdit);

        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_UPDATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> deleteProduct(String productID) {
        Product product = iProductRepository.findById(UUID.fromString(productID)).orElse(null);
        if (product == null){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        iProductRepository.delete(product);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }
}
