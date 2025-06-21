package com.gentlemonster.GentleMonsterBE.Services.WarehouseProduct;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.WarehouseProduct.AddProductWarehouseRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.WarehouseProduct.EditProductWarehouseRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.WarehouseProduct.WarehouseProductRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.WarehouseProduct.BaseProductWarehouseResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.WarehouseProduct.ProductWarehouseResponse;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IWarehouseProductService {
    PagingResponse<List<BaseProductWarehouseResponse>> getAllProductInWarehouse(WarehouseProductRequest warehouseProductRequest);
    APIResponse<Boolean> addProductToWarehouse(AddProductWarehouseRequest addProductWarehouseRequest);
    APIResponse<Boolean> editProductInWarehouse(String id ,EditProductWarehouseRequest editProductWarehouseRequest);
    APIResponse<Boolean> deleteProductInWarehouse(String id);
    APIResponse<ProductWarehouseResponse> getProductInWarehouse(String id);
    APIResponse<Boolean> uploadMediaProductInWarehouse(String warehouseProductID, MultipartFile file);
}
