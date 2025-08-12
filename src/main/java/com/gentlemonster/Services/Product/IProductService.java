package com.gentlemonster.Services.Product;

import com.gentlemonster.DTO.Requests.Product.AddProductRequest;
import com.gentlemonster.DTO.Requests.Product.EditProductRequest;
import com.gentlemonster.DTO.Requests.Product.ProductRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.Product.BaseProductResponse;
import com.gentlemonster.DTO.Responses.Product.ProductResponse;
import com.gentlemonster.DTO.Responses.Product.Public.BaseProductPublicResponse;
import com.gentlemonster.DTO.Responses.Product.Public.ProductDetailPublicResponse;
import com.gentlemonster.Exception.NotFoundException;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    PagingResponse<List<BaseProductResponse>> getAllProduct(ProductRequest productRequest);
    APIResponse<ProductResponse> getOneProduct(String productID) throws NotFoundException;
    APIResponse<Boolean> addProduct(AddProductRequest addProductRequest) throws NotFoundException;
    APIResponse<Boolean> editProduct(String productID, EditProductRequest editProductRequest) throws NotFoundException;
    APIResponse<Boolean> deleteProduct(String productID) throws NotFoundException;

    APIResponse<ProductDetailPublicResponse> getProductDetailPublic(String slug, String productCode) throws NotFoundException;
    APIResponse<List<BaseProductPublicResponse>> getAllProductPublic(String categorySlug, String sliderSlug) throws NotFoundException;

    APIResponse<Boolean> handleUploadImages(String productID, MultipartFile[] images) throws NotFoundException;
}
