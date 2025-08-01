package com.gentlemonster.GentleMonsterBE.Services.Product;

import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Product.AddProductRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Product.EditProductRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Product.ProductRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.BaseProductResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.ProductResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.Public.BaseProductPublicResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.Public.ProductDetailPublicResponse;
import com.gentlemonster.GentleMonsterBE.Entities.*;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;
import com.gentlemonster.GentleMonsterBE.Repositories.*;
import com.gentlemonster.GentleMonsterBE.Repositories.Specification.ProductSpecification;
import com.gentlemonster.GentleMonsterBE.Services.Cloudinary.CloudinaryService;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtils;
import com.gentlemonster.GentleMonsterBE.Utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private LocalizationUtils localizationUtil;
    @Autowired
    private ValidationUtils vietnameseStringUtils;
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
    public APIResponse<ProductResponse> getOneProduct(String productID) throws NotFoundException {
        Product product = iProductRepository.findById(UUID.fromString(productID)).orElse(null);
        if(product == null){
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_NOT_FOUND));
        }
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_GET_SUCCESS));
        return new APIResponse<>(productResponse, messages);
    }

    @Override
    public APIResponse<Boolean> addProduct(AddProductRequest addProductRequest) throws NotFoundException {
        if (!iWarehouseProductRepository.existsByProductName(addProductRequest.getProductName())){
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_NOT_FOUND));
        }
        if (iWarehouseProductRepository.existsByPublicProductFalse()){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_WAREHOUSE_DONT_PUBLIC));
            return new APIResponse<>(false, messages);
        }
        Product product = modelMapper.map(addProductRequest, Product.class);
        product.setName(addProductRequest.getProductName());
        String slugStandardization = vietnameseStringUtils.removeAccentsAndSpaces(addProductRequest.getProductName());
        product.setSlug(slugStandardization);
        product.setCode(generateCode(slugStandardization));
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
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND));
        }
        product.setProductType(productType);
        iProductRepository.save(product);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_CREATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> editProduct(String productID, EditProductRequest editProductRequest) throws NotFoundException {
        // Tìm sản phẩm theo ID
        Product productEdit = iProductRepository.findById(UUID.fromString(productID)).orElse(null);
        if (productEdit == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_NOT_FOUND));
        }
        if (iWarehouseProductRepository.existsByPublicProductFalse()){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_WAREHOUSE_DONT_PUBLIC));
            return new APIResponse<>(false, messages);
        }
        modelMapper.map(editProductRequest, productEdit);
        // Cập nhật các trường của Product
        productEdit.setName(editProductRequest.getProductName());
        String slugStandardization = vietnameseStringUtils.removeAccentsAndSpaces(editProductRequest.getProductName())
                .toLowerCase().replaceAll("\\s+", "-").trim();
        productEdit.setSlug(slugStandardization);
        productEdit.setCode(generateCode(slugStandardization));
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
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND));
        }
        productEdit.setProductType(productType);

        // Lưu sản phẩm (sẽ tự động lưu hoặc cập nhật ProductDetail nhờ CascadeType.ALL)
        iProductRepository.save(productEdit);

        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_UPDATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> deleteProduct(String productID) throws NotFoundException {
        Product product = iProductRepository.findById(UUID.fromString(productID)).orElse(null);
        if (product == null){
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_NOT_FOUND));
        }

        //  Kiểm tra null ProductDetail trước khi truy xuất danh sách ảnh
        if (product.getProductDetail() != null && product.getProductDetail().getImage() != null) {
            product.getProductDetail().getImage().stream()
                .filter(media -> media != null && media.getPublicId() != null)
                .map(Media::getPublicId)
                .forEach(cloudinaryService::deleteMedia);
        }

        iProductRepository.delete(product);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<ProductDetailPublicResponse> getProductDetailPublic(String productSlug, String productCode) throws NotFoundException {
        Specification<Product> specification = ProductSpecification.getOneProductBySlugAndCode(productSlug, productCode);
        Product product = iProductRepository.findOne(specification).orElse(null);
        if (product == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_NOT_FOUND));
        };
        ProductDetailPublicResponse productDetailPublicResponse = modelMapper.map(product, ProductDetailPublicResponse.class);
        ArrayList <String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_GET_SUCCESS));
        return new APIResponse<>(productDetailPublicResponse, messages);
    }

    // @Override
    // public APIResponse<Boolean> uploadProductImage(String productID, MultipartFile[] images, String type) {
    //     try{
    //         if ("THUMBNAIL".equalsIgnoreCase(type)) {
    //             if (images == null || images.length != 1 || images[0] == null || images[0].isEmpty()) {
    //                 return new APIResponse<>(false, List.of("Exactly one thumbnail file is required"));
    //             }
    //             return handleUploadThumbnail(productID, images[0]);
    //         }else if("GALLERY".equalsIgnoreCase(type)){
    //             if (images == null || images.length == 0) {
    //                 return new APIResponse<>(false, List.of("At least one detail image file is required"));
    //             }
    //             return handleUploadImages(productID, images);
    //         }else{
    //             List<String> messages = new ArrayList<>();
    //             messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_UPLOAD_MEDIA_FAILED));
    //             return new APIResponse<>(false, messages);
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         List<String> messages = new ArrayList<>();
    //         messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_UPLOAD_MEDIA_FAILED));
    //         return new APIResponse<>(false, messages);
    //     }
            
    // }

    // private APIResponse<Boolean> handleUploadThumbnail(String productID, MultipartFile thumbnail) throws NotFoundException{
    //     Product product = iProductRepository.findById(UUID.fromString(productID)).orElse(null);
    //     if (product == null) {
    //         // List<String> messages = new ArrayList<>();
    //         // messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_NOT_FOUND));
    //         // return new APIResponse<>(false, messages);
    //         throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_NOT_FOUND));
    //     }
    //     try {
    //         if (product.getThumbnailMedia() != null) {
    //             // Xóa media cũ nếu có
    //             cloudinaryService.deleteMedia(product.getThumbnailMedia().getPublicId());
    //             product.setThumbnailMedia(new Media());
    //         }
    //         Map uploadResult = cloudinaryService.uploadMedia(thumbnail, "products");
    //         String imageUrl = (String) uploadResult.get("secure_url");
    //         Media thumbnailMedia = Media.builder()
    //                 .imageUrl(imageUrl)
    //                 .publicId((String) uploadResult.get("public_id"))
    //                 .referenceId(product.getId())
    //                 .referenceType("PRODUCT")
    //                 .altText("Thumbnail for product: " + product.getName())
    //                 .type("THUMBNAIL")
    //                 .build();
    //         product.setThumbnailMedia(thumbnailMedia);
    //         iProductRepository.save(product);
    //         List<String> messages = new ArrayList<>();
    //         messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_UPLOAD_THUMBNAIL_SUCCESS));
    //         return new APIResponse<>(true, messages);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         List<String> messages = new ArrayList<>();
    //         messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_UPLOAD_THUMBNAIL_FAILED));
    //         return new APIResponse<>(false, messages);
    //     }
    // }
    
    @Override
    public APIResponse<Boolean> handleUploadImages(String productID, MultipartFile[] images) throws NotFoundException{
        Product product = iProductRepository.findById(UUID.fromString(productID)).orElse(null);
        if (product == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_NOT_FOUND));
        }
        ProductDetail productDetail = product.getProductDetail();
        if (productDetail == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_DETAIL_NOT_FOUND));
        }
        try {
            productDetail.getImage().stream()
                .filter(media -> "GALLERY".equals(media.getType()) && media.getPublicId() != null)
                .forEach(media -> {
                    cloudinaryService.deleteMedia(media.getPublicId());
                });
            productDetail.getImage().clear();

            List<Media> newImages = Arrays.stream(images)
                .filter(image -> image != null && !image.isEmpty())
                .map(image -> {
                        String originalFilename = image.getOriginalFilename();
                        if (originalFilename == null || !originalFilename.contains(".")) {
                            throw new RuntimeException("Invalid file name: " + (originalFilename != null ? originalFilename : "null"));
                        }
                         // Upload image
                        Map uploadResult = cloudinaryService.uploadMedia(image, "products");
                        String imageUrl = (String) uploadResult.get("secure_url");
                        System.out.println("Gallery image URL: " + imageUrl);
                        // Create new Media
                        return Media.builder()
                                .imageUrl(imageUrl)
                                .publicId((String) uploadResult.get("public_id"))
                                .referenceId(product.getId())
                                .referenceType("PRODUCT")
                                .altText("Detail image for product: " + product.getName())
                                .type("GALLERY")
                                .build();
                }).collect(Collectors.toList());

            if (newImages.isEmpty()) {
                List<String> messages = new ArrayList<>();
                messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_DETAIL_NOT_VALID_FILES_UPLOADED));
                return new APIResponse<>(false, messages);
            }
            productDetail.getImage().addAll(newImages);
            iProductDetailRepository.save(productDetail);
            System.out.println("Product detail images uploaded successfully: " + newImages.size() + " images");
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

   @Override
   public APIResponse<List<BaseProductPublicResponse>> getAllProductPublic(String categorySlug, String sliderSlug) throws NotFoundException {
        List<BaseProductPublicResponse> productPublicResponseList;
        List<Product> productList;
        List<String> targetCategories = Arrays.asList("sunglasses", "glasses");
        // Trường hợp đặc biệt: categorySlug = collaboration
        if ("collaboration".equalsIgnoreCase(categorySlug)) {
                // Lấy các slider được đánh dấu highlighted = true và nằm trong 2 category trên
                List<Slider> categoryCollaboration = iSliderRepository.findAll().stream()
                    .filter(slider -> slider.isHighlighted() && slider. getCategory() != null && targetCategories.contains(slider.getCategory().getSlug().toLowerCase()))
                    .toList();

                // Nếu là /collaboration/view-all → lấy toàn bộ productType của các slider hợp lệ
                if ("view-all".equalsIgnoreCase(sliderSlug)) {
                    if (categoryCollaboration.isEmpty()) {
                        throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
                    }
                    Specification<Product> listProductSpecification = ProductSpecification.getListProductBySliders(categoryCollaboration);
                    productList = iProductRepository.findAll(listProductSpecification);
                }else{
                    // /collaboration/{sliderSlug} → tìm sliderSlug có trong collaborationSliders
                    Slider slider = categoryCollaboration.stream()
                        .filter(s -> s.getSlug().equalsIgnoreCase(sliderSlug))
                        .findFirst()
                        .orElse(null);
                    if (slider == null) {
                        throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
                    }
                    Specification<Product> productSpecification = ProductSpecification.getListProductBySlider(sliderSlug);
                    productList = iProductRepository.findAll(productSpecification);
                }
                productPublicResponseList = productList.stream().map(product -> modelMapper.map(product, BaseProductPublicResponse.class)).toList();
                if (productPublicResponseList.isEmpty()) {
                    List<String> messages = new ArrayList<>();
                    messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_EMPTY));
                    return new APIResponse<>(productPublicResponseList, messages);
                }   

                return new APIResponse<>(productPublicResponseList, 
                    List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_GET_SUCCESS)));
        }
        // Trường hợp bình thường: kiểm tra category hợp lệ
            Category category = iCategoryRepository.findBySlug(categorySlug).orElse(null);
            if (category == null) {
                throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND));
            }
            if("view-all".equalsIgnoreCase(sliderSlug)){
                Specification<Product> specification = ProductSpecification.getListProductByCategorySlug(categorySlug);
                productList = iProductRepository.findAll(specification);
            }else{
                Slider slider = iSliderRepository.findBySlug(sliderSlug).orElse(null);
                if (slider == null) {
                    throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
                }
                Specification<Product> specification = ProductSpecification.getListProduct(categorySlug, sliderSlug);
                productList = iProductRepository.findAll(specification);
            }
            productPublicResponseList = productList.stream()
                .map(product -> modelMapper.map(product, BaseProductPublicResponse.class))
                .toList();
            if (productPublicResponseList.isEmpty()) {
                return new APIResponse<>(productPublicResponseList,
                        List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_EMPTY)));
            }
        return new APIResponse<>(productPublicResponseList,
                    List.of(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_GET_SUCCESS)));
    }


    String generateCode(String input) {
        String code;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        do {
            code = IntStream.range(0, 12)
                    .mapToObj(i -> String.valueOf(characters.charAt(random.nextInt(characters.length()))))
                    .collect(Collectors.joining());
        } while (iProductRepository.existsByCode(code));
        return code;
    }

}
