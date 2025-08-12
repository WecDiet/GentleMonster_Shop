package com.gentlemonster.Services.ProductType;

import com.gentlemonster.DTO.Requests.ProductType.AddProductTypeRequest;
import com.gentlemonster.DTO.Requests.ProductType.EditProductTypeRequest;
import com.gentlemonster.DTO.Requests.ProductType.ProductTypeRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.ProductType.BaseProductTypeResponse;
import com.gentlemonster.DTO.Responses.ProductType.ProductTypeResponse;
import com.gentlemonster.Exception.NotFoundException;

import java.util.List;

public interface IProductTypeService {
    PagingResponse<List<BaseProductTypeResponse>> getAllProductType(ProductTypeRequest productTypeRequest);
    APIResponse<Boolean> addProductType(AddProductTypeRequest addProductTypeRequest) throws NotFoundException;
    APIResponse<Boolean> editProductType(String productTypeID, EditProductTypeRequest editProductTypeRequest) throws NotFoundException;
    APIResponse<Boolean> deleteProductType(String productTypeID) throws NotFoundException;
    APIResponse<ProductTypeResponse> getProductType(String productTypeID) throws NotFoundException;
    // APIResponse<List<ProductPublicResponse>> getAllProductTypePublic(String categorySlug,String sliderSlug);
}
