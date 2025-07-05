package com.gentlemonster.GentleMonsterBE.Services.Store;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Store.AddStoreRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Store.EditStoreRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Store.StoreRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Store.BaseStoreResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Store.StorePublicResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Store.StoreResponse;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;

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
