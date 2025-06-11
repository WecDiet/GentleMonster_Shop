package com.gentlemonster.GentleMonsterBE.Services.Product;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Product.AddProductRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Product.EditProductRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Product.ProductRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.BaseProductResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.ProductResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.Public.ProductDetailPublicResponse;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    PagingResponse<List<BaseProductResponse>> getAllProduct(ProductRequest productRequest);
    APIResponse<ProductResponse> getOneProduct(String productID);
    APIResponse<Boolean> addProduct(AddProductRequest addProductRequest);
    APIResponse<Boolean> editProduct(String productID, EditProductRequest editProductRequest);
    APIResponse<Boolean> deleteProduct(String productID);

    APIResponse<ProductDetailPublicResponse> getProductDetailPublic(String productTypeName, String productID);
//    APIResponse<List<ProductPublicResponse>> getAllProductTypePublic(String categorySlug, String sliderSlug);

    APIResponse<Boolean> uploadProductImage(String productID, MultipartFile file);
}
