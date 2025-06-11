package com.gentlemonster.GentleMonsterBE.Services.Product;

import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Product.AddProductRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Product.EditProductRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Product.ProductRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.BaseProductResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.ProductResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.Public.ProductDetailPublicResponse;
import com.gentlemonster.GentleMonsterBE.Entities.*;
import com.gentlemonster.GentleMonsterBE.Repositories.*;
import com.gentlemonster.GentleMonsterBE.Services.Cloudinary.CloudinaryService;
import com.gentlemonster.GentleMonsterBE.Utils.FileUploadUtil;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtil;
import com.gentlemonster.GentleMonsterBE.Utils.VietnameseStringUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private ICategoryRepository iCategoryRepository;
    @Autowired
    private ISliderRepository iSliderRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

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
        String slugStandardization = vietnameseStringUtils.removeAccents(addProductRequest.getProductName());
        product.setSlug(slugStandardization);
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
        modelMapper.map(editProductRequest, productEdit);
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

    @Override
    public APIResponse<ProductDetailPublicResponse> getProductDetailPublic(String productTypeName, String productID) {
        Product product = iProductRepository.findById(UUID.fromString(productID)).orElse(null);
        if (product == null){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_NOT_FOUND));
            return new APIResponse<>(null, messages);
        }
        if (!product.getProductType().getName().equals(productTypeName)){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND));
            return new APIResponse<>(null, messages);
        }
        ProductDetailPublicResponse productDetailPublicResponse = modelMapper.map(product, ProductDetailPublicResponse.class);
        ArrayList <String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_GET_SUCCESS));
        return new APIResponse<>(productDetailPublicResponse, messages);
    }

    @Override
    public APIResponse<Boolean> uploadProductImage(String productID, MultipartFile image) {
        Product product = iProductRepository.findById(UUID.fromString(productID)).orElse(null);
        if (product == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
            try {
                Map uploadResult = cloudinaryService.uploadMedia(image, "products");
                String imageUrl = (String) uploadResult.get("secure_url");
                System.out.println("Image URL: " + imageUrl);
                Media media = Media.builder()
                        .imageUrl(imageUrl)
                        .publicId((String) uploadResult.get("public_id"))
                        .referenceId(product.getId())
                        .referenceType("PRODUCT")
                        .altText("Product detail photo: " + product.getName())
                        .type("GALLERY")
                        .build();
                product.getMedias().add(media);
                iProductRepository.save(product);

                System.out.println("Product image uploaded successfully: " + imageUrl);
                List<String> messages = new ArrayList<>();
                messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_UPLOAD_MEDIA_SUCCESS));
                return new APIResponse<>(true, messages);
            } catch (Exception e) {
                e.printStackTrace();
                List<String> messages = new ArrayList<>();
                messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_UPLOAD_MEDIA_FAILED));
                return new APIResponse<>(false, messages);
            }
            
    }
//    @Override
//    public APIResponse<List<ProductPublicResponse>> getAllProductTypePublic(String categorySlug, String sliderSlug) {
//        List<ProductPublicResponse> productPublicResponseList;
//        List<Product> productList;
//        Category category = iCategoryRepository.findBySlug(categorySlug).orElse(null);
//        if (category == null){
//            List<String> messages = new ArrayList<>();
//            messages.add(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND));
//            return new APIResponse<>(null, messages);
//        }
//        if ("view-all".equals(sliderSlug) && category.getSlug().equals(categorySlug)){
//            Specification<Product> specification = ProductSpecification.getListProductByCategorySlug(categorySlug);
//            productList = iProductRepository.findAll(specification);
//            productPublicResponseList = productList.stream()
//                    .map(product -> modelMapper.map(product, ProductPublicResponse.class))
//                    .toList();
//            if (productPublicResponseList.isEmpty()){
//                List<String> messages = new ArrayList<>();
//                messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_EMPTY));
//                return new APIResponse<>(productPublicResponseList, messages);
//            }
//            List<String> messages = new ArrayList<>();
//            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_GET_SUCCESS));
//            return new APIResponse<>(productPublicResponseList, messages);
//        }
//        Slider slider = iSliderRepository.findBySlug(sliderSlug).orElse(null);
//        if (slider == null){
//            List<String> messages = new ArrayList<>();
//            messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
//            return new APIResponse<>(null, messages);
//        }
//        Specification<Product> specification = ProductSpecification.getListProduct(categorySlug, sliderSlug);
//        productList = iProductRepository.findAll(specification);
//        productPublicResponseList = productList.stream()
//                .map(product -> modelMapper.map(product, ProductPublicResponse.class))
//                .toList();
//        if (productPublicResponseList.isEmpty()){
//            List<String> messages = new ArrayList<>();
//            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_EMPTY));
//            return new APIResponse<>(productPublicResponseList, messages);
//        }
//        List<String> messages = new ArrayList<>();
//        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_GET_SUCCESS));
//        return new APIResponse<>(productPublicResponseList, messages);
//    }

}
