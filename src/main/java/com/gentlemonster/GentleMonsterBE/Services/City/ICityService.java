package com.gentlemonster.GentleMonsterBE.Services.City;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.City.AddCityRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.City.CityRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.City.EditCityRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.City.CityResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ICityService {
    PagingResponse<List<CityResponse>> getAllCities(CityRequest cityRequest);
    APIResponse<Boolean> addCity(AddCityRequest addCityRequest);
    APIResponse<Boolean> editCity(String cityID, EditCityRequest editCityRequest);
    APIResponse<Boolean> deleteCity(String cityID);
    APIResponse<Boolean> uploadImageCity(String cityID, MultipartFile image);
}
