package com.gentlemonster.GentleMonsterBE.Services.Warehouse;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Warehouse.AddWarehouseRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Warehouse.EditWarehouseRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Warehouse.WarehouseRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Warehouse.BaseWarehouseResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Warehouse.WarehouseResponse;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;

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
