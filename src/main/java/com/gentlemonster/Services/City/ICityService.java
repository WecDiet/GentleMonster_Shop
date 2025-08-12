package com.gentlemonster.Services.City;

import com.gentlemonster.DTO.Requests.City.AddCityRequest;
import com.gentlemonster.DTO.Requests.City.CityRequest;
import com.gentlemonster.DTO.Requests.City.EditCityRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.City.CityResponse;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.DTO.Responses.PagingResponse;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ICityService {
    PagingResponse<List<CityResponse>> getAllCities(CityRequest cityRequest);
    APIResponse<Boolean> addCity(AddCityRequest addCityRequest) throws NotFoundException;
    APIResponse<Boolean> editCity(String cityID, EditCityRequest editCityRequest) throws NotFoundException;
    APIResponse<Boolean> deleteCity(String cityID) throws NotFoundException;
    APIResponse<Boolean> uploadImageCity(String cityID, MultipartFile image) throws NotFoundException;
}
