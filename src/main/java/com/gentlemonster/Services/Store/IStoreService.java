package com.gentlemonster.Services.Store;

import com.gentlemonster.DTO.Requests.Store.AddStoreRequest;
import com.gentlemonster.DTO.Requests.Store.EditStoreRequest;
import com.gentlemonster.DTO.Requests.Store.StoreRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.Store.BaseStoreResponse;
import com.gentlemonster.DTO.Responses.Store.StorePublicResponse;
import com.gentlemonster.DTO.Responses.Store.StoreResponse;
import com.gentlemonster.Exception.NotFoundException;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IStoreService {
    PagingResponse<List<BaseStoreResponse>> getAllStore(StoreRequest storeRequest);
    APIResponse<Boolean> addStore(AddStoreRequest addStoreRequest) throws NotFoundException;
    APIResponse<Boolean> editStore(String storeID, EditStoreRequest editStoreRequest) throws NotFoundException;
    APIResponse<Boolean> deleteStore(String storeID) throws NotFoundException;
    APIResponse<StoreResponse> getOneStore(String storeID) throws NotFoundException;
    APIResponse<List<StorePublicResponse>> getAllStoreByCountry(StoreRequest storeRequest) throws NotFoundException;
    APIResponse<Boolean> handleUploadGallery(String storeID, MultipartFile[] images) throws NotFoundException;
}
