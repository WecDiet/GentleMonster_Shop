package com.gentlemonster.GentleMonsterBE.Services.Store;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Store.AddStoreRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Store.EditStoreRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Store.StoreRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Store.BaseStoreResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Store.StorePublicResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Store.StoreResponse;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public interface IStoreService {
    PagingResponse<List<BaseStoreResponse>> GetAllStore(StoreRequest storeRequest);
    APIResponse<Boolean> AddStore(AddStoreRequest addStoreRequest);
    APIResponse<Boolean> EditStore(String storeID, EditStoreRequest editStoreRequest);
    APIResponse<Boolean> DeleteStore(String storeID);
    APIResponse<StoreResponse> GetOneStore(String storeID);
    APIResponse<List<StorePublicResponse>> GetAllStoreByCountry(StoreRequest storeRequest);

}
