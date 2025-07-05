package com.gentlemonster.GentleMonsterBE.Services.City;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.City.AddCityRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.City.CityRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.City.EditCityRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.City.CityResponse;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ICityService {
    PagingResponse<List<CityResponse>> getAllCities(CityRequest cityRequest);
    APIResponse<Boolean> addCity(AddCityRequest addCityRequest) throws NotFoundException;
    APIResponse<Boolean> editCity(String cityID, EditCityRequest editCityRequest) throws NotFoundException;
    APIResponse<Boolean> deleteCity(String cityID) throws NotFoundException;
    APIResponse<Boolean> uploadImageCity(String cityID, MultipartFile image) throws NotFoundException;
}
