package com.gentlemonster.Services.WarehouseProduct;

import com.gentlemonster.Contants.MessageKey;
import com.gentlemonster.DTO.Requests.WarehouseProduct.AddProductWarehouseRequest;
import com.gentlemonster.DTO.Requests.WarehouseProduct.EditProductWarehouseRequest;
import com.gentlemonster.DTO.Requests.WarehouseProduct.WarehouseProductRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.WarehouseProduct.BaseProductWarehouseResponse;
import com.gentlemonster.DTO.Responses.WarehouseProduct.ProductWarehouseResponse;
import com.gentlemonster.Entities.Media;
import com.gentlemonster.Entities.Warehouse;
import com.gentlemonster.Entities.WarehouseProduct;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Repositories.IProductRepository;
import com.gentlemonster.Repositories.IWarehouseProductRepository;
import com.gentlemonster.Repositories.IWarehouseRepository;
import com.gentlemonster.Services.Cloudinary.CloudinaryService;
import com.gentlemonster.Utils.LocalizationUtils;
import com.gentlemonster.Utils.ValidationUtils;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@NoArgsConstructor
public class WarehouseProductService implements IWarehouseProductService {

    @Autowired
    private IWarehouseProductRepository warehouseProductRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ValidationUtils vietnameseStringUtils;
    @Autowired
    private LocalizationUtils localizationUtil;
    @Autowired
    private IWarehouseRepository warehouseRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public PagingResponse<List<BaseProductWarehouseResponse>> getAllProductInWarehouse(
            WarehouseProductRequest warehouseProductRequest) {
        List<BaseProductWarehouseResponse> baseProductWarehouseResponses;
        List<WarehouseProduct> warehouseProducts;
        Pageable pageable;
        if (warehouseProductRequest.getPage() == 0 && warehouseProductRequest.getLimit() == 0) {
            warehouseProducts = warehouseProductRepository.findAll();
            baseProductWarehouseResponses = warehouseProducts.stream()
                    .map(warehouseProduct -> modelMapper.map(warehouseProduct, BaseProductWarehouseResponse.class))
                    .toList();
            // baseProductWarehouseResponses = warehouseProducts.stream()
            // .map(warehouseProduct -> modelMapper.typeMap(WarehouseProduct.class,
            // BaseProductWarehouseResponse.class)
            // .addMapping(WarehouseProduct::getImportDate,
            // BaseProductWarehouseResponse::setImportDates)
            // .map(warehouseProduct))
            // .toList();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_WAREHOUSE_GET_SUCCESS));
            return new PagingResponse<>(baseProductWarehouseResponses, messages, 1,
                    (long) baseProductWarehouseResponses.size());
        } else {
            warehouseProductRequest.setPage(Math.max(warehouseProductRequest.getPage(), 1));
            pageable = PageRequest.of(warehouseProductRequest.getPage() - 1, warehouseProductRequest.getLimit());
        }
        Page<WarehouseProduct> warehouseProductPage = warehouseProductRepository.findAll(pageable);
        warehouseProducts = warehouseProductPage.getContent();
        baseProductWarehouseResponses = warehouseProducts.stream()
                .map(warehouseProduct -> modelMapper.map(warehouseProduct, BaseProductWarehouseResponse.class))
                .toList();

        if (baseProductWarehouseResponses.isEmpty()) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_WAREHOUSE_EMPTY));
            return new PagingResponse<>(baseProductWarehouseResponses, messages, 1, 0L);
        }

        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_WAREHOUSE_GET_SUCCESS));
        return new PagingResponse<>(baseProductWarehouseResponses, messages, warehouseProductPage.getTotalPages(),
                warehouseProductPage.getTotalElements());
    }

    @Override
    public APIResponse<Boolean> addProductToWarehouse(AddProductWarehouseRequest addProductWarehouseRequest)
            throws NotFoundException {
        Warehouse warehouse = warehouseRepository.findByWarehouseName(addProductWarehouseRequest.getWareHouseName())
                .orElse(null);
        if (warehouse == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
        }
        WarehouseProduct warehouseProduct = modelMapper.map(addProductWarehouseRequest, WarehouseProduct.class);
        warehouseProduct.setWarehouse(warehouse);
        warehouseProduct.setImportDate(LocalDateTime.of(addProductWarehouseRequest.getImportYear(),
                addProductWarehouseRequest.getImportMonth(), addProductWarehouseRequest.getImportDay(), 0, 0));
        warehouseProduct.setModifiedDate(LocalDateTime.now().toLocalDate().atStartOfDay());
        BigInteger totalImportPrice = warehouseProduct.getImportPrice()
                .multiply(BigInteger.valueOf(warehouseProduct.getQuantity().longValue()));
        warehouseProduct.setTotalImportPrice(totalImportPrice);
        warehouseProductRepository.save(warehouseProduct);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_CREATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> editProductInWarehouse(String id,
            EditProductWarehouseRequest editProductWarehouseRequest) throws NotFoundException {
        Warehouse warehouse = warehouseRepository.findByWarehouseName(editProductWarehouseRequest.getWareHouseName())
                .orElse(null);
        WarehouseProduct warehouseProduct = warehouseProductRepository.findById(UUID.fromString(id)).orElse(null);
        if (warehouse == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
        }
        if (warehouseProduct == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
        }
        modelMapper.map(editProductWarehouseRequest, warehouseProduct);
        warehouseProduct.setWarehouse(warehouse);
        warehouseProduct.setModifiedDate(LocalDateTime.now());
        warehouseProductRepository.save(warehouseProduct);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_UPDATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> deleteProductInWarehouse(String id) throws NotFoundException {
        WarehouseProduct warehouseProduct = warehouseProductRepository.findById(UUID.fromString(id)).orElse(null);
        if (warehouseProduct == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
        }
        warehouseProductRepository.delete(warehouseProduct);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<ProductWarehouseResponse> getProductInWarehouse(String id) throws NotFoundException {
        WarehouseProduct warehouseProduct = warehouseProductRepository.findById(UUID.fromString(id)).orElse(null);
        if (warehouseProduct == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
        }
        // ProductWarehouseResponse productWarehouseResponse =
        // modelMapper.map(warehouseProduct, ProductWarehouseResponse.class);
        ProductWarehouseResponse productWarehouseResponse = modelMapper
                .typeMap(WarehouseProduct.class, ProductWarehouseResponse.class)
                .addMapping(WarehouseProduct::getImportDate, ProductWarehouseResponse::setImportDate)
                .map(warehouseProduct);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_GET_SUCCESS));
        return new APIResponse<>(productWarehouseResponse, messages);
    }

    @Override
    public APIResponse<Boolean> uploadMediaProductInWarehouse(String warehouseProductID, MultipartFile file)
            throws NotFoundException {
        WarehouseProduct warehouseProduct = warehouseProductRepository.findById(UUID.fromString(warehouseProductID))
                .orElse(null);
        if (warehouseProduct == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
        }

        try {
            Map uploadResult = cloudinaryService.uploadMedia(file, "warehouse_product");
            String imageUrl = (String) uploadResult.get("secure_url");
            warehouseProduct.getImage().add(Media.builder()
                    .imageUrl(imageUrl)
                    .publicId((String) uploadResult.get("public_id"))
                    .altText("product photos in stock: " + warehouseProduct.getProductName())
                    .referenceId(warehouseProduct.getId())
                    .referenceType("WAREHOUSE_PRODUCT")
                    .type("IMAGE")
                    .build());
            warehouseProduct.setImage(warehouseProduct.getImage());
            warehouseProductRepository.save(warehouseProduct);
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_WAREHOUSE_UPLOAD_MEDIA_SUCCESS));
            return new APIResponse<>(true, messages);
        } catch (Exception e) {
            e.printStackTrace();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_WAREHOUSE_UPLOAD_MEDIA_FAILED));
            return new APIResponse<>(false, messages);
        }
    }

    @Override
    public APIResponse<Boolean> exportProduct(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exportProduct'");
    }
}
