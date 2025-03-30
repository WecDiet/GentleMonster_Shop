package com.gentlemonster.GentleMonsterBE.Services.Warehouse;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Warehouse.AddWarehouseRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Warehouse.EditWarehouseRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Warehouse.WarehouseRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Warehouse.BaseWarehouseResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Warehouse.WarehouseResponse;

import java.util.List;

public interface IWarehouseService {
    PagingResponse<List<BaseWarehouseResponse>> getAllWarehouse(WarehouseRequest warehouseRequest);
    APIResponse<Boolean> addWarehouse(AddWarehouseRequest addWarehouseRequest);
    APIResponse<Boolean> updateWarehouse(String warehouseID ,EditWarehouseRequest editWarehouseRequest);
    APIResponse<Boolean> deleteWarehouse(String warehouseId);
    APIResponse<WarehouseResponse> getWarehouseById(String warehouseId);
//    PagingResponse<>
}
