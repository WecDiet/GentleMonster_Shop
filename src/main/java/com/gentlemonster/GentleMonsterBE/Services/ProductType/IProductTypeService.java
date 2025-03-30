package com.gentlemonster.GentleMonsterBE.Services.ProductType;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.ProductType.AddProductTypeRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.ProductType.EditProductTypeRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.ProductType.ProductTypeRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.ProductType.BaseProductTypeResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.ProductType.ProductTypeResponse;

import java.util.List;

public interface IProductTypeService {
    PagingResponse<List<BaseProductTypeResponse>> getAllProductType(ProductTypeRequest productTypeRequest);
    APIResponse<Boolean> addProductType(AddProductTypeRequest addProductTypeRequest);
    APIResponse<Boolean> editProductType(String productTypeID, EditProductTypeRequest editProductTypeRequest);
    APIResponse<Boolean> deleteProductType(String productTypeID);
    APIResponse<ProductTypeResponse> getProductType(String productTypeID);
}
