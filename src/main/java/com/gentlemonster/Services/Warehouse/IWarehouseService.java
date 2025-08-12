package com.gentlemonster.Services.Warehouse;

import com.gentlemonster.DTO.Requests.Warehouse.AddWarehouseRequest;
import com.gentlemonster.DTO.Requests.Warehouse.EditWarehouseRequest;
import com.gentlemonster.DTO.Requests.Warehouse.WarehouseRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.Warehouse.BaseWarehouseResponse;
import com.gentlemonster.DTO.Responses.Warehouse.WarehouseResponse;
import com.gentlemonster.Exception.NotFoundException;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IWarehouseService {
    PagingResponse<List<BaseWarehouseResponse>> getAllWarehouse(WarehouseRequest warehouseRequest);
    APIResponse<Boolean> addWarehouse(AddWarehouseRequest addWarehouseRequest);
    APIResponse<Boolean> updateWarehouse(String warehouseID ,EditWarehouseRequest editWarehouseRequest) throws NotFoundException;
    APIResponse<Boolean> deleteWarehouse(String warehouseId) throws NotFoundException;
    APIResponse<WarehouseResponse> getWarehouseById(String warehouseId) throws NotFoundException;
    APIResponse<Boolean> handleUploadImage(String warehouseID, MultipartFile[] images) throws NotFoundException;
}
