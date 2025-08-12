package com.gentlemonster.Services.WarehouseProduct;

import com.gentlemonster.DTO.Requests.WarehouseProduct.AddProductWarehouseRequest;
import com.gentlemonster.DTO.Requests.WarehouseProduct.EditProductWarehouseRequest;
import com.gentlemonster.DTO.Requests.WarehouseProduct.WarehouseProductRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.WarehouseProduct.BaseProductWarehouseResponse;
import com.gentlemonster.DTO.Responses.WarehouseProduct.ProductWarehouseResponse;
import com.gentlemonster.Exception.NotFoundException;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IWarehouseProductService {
    PagingResponse<List<BaseProductWarehouseResponse>> getAllProductInWarehouse(WarehouseProductRequest warehouseProductRequest);
    APIResponse<Boolean> addProductToWarehouse(AddProductWarehouseRequest addProductWarehouseRequest) throws NotFoundException;
    APIResponse<Boolean> editProductInWarehouse(String id ,EditProductWarehouseRequest editProductWarehouseRequest) throws NotFoundException;
    APIResponse<Boolean> deleteProductInWarehouse(String id) throws NotFoundException;
    APIResponse<ProductWarehouseResponse> getProductInWarehouse(String id) throws NotFoundException;
    APIResponse<Boolean> uploadMediaProductInWarehouse(String warehouseProductID, MultipartFile file) throws NotFoundException;
}
