package com.gentlemonster.GentleMonsterBE.Services.City;

import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.City.AddCityRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.City.CityRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.City.EditCityRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.City.CityResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.Entities.City;
import com.gentlemonster.GentleMonsterBE.Entities.Media;
import com.gentlemonster.GentleMonsterBE.Enums.StoreEnum;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;
import com.gentlemonster.GentleMonsterBE.Repositories.ICityRepository;
import com.gentlemonster.GentleMonsterBE.Services.Cloudinary.CloudinaryService;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtils;
import com.gentlemonster.GentleMonsterBE.Utils.ValidationUtils;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@NoArgsConstructor
public class CityService implements ICityService{
    @Autowired
    private ICityRepository iCityRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LocalizationUtils localizationUtil;
    @Autowired
    private ValidationUtils vietnameseStringUtils;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public PagingResponse<List<CityResponse>> getAllCities(CityRequest cityRequest) {
        List<CityResponse> cityResponseList;
        List<City> cityList;
        Pageable pageable;
        if (cityRequest.getLimit() == 0 && cityRequest.getPage() == 0){
            cityList = iCityRepository.findAll();
            cityResponseList = cityList.stream()
                    .map(city -> modelMapper.map(city, CityResponse.class))
                    .toList();
            if (cityResponseList.isEmpty()){
                List<String> messages = new ArrayList<>();
                messages.add(localizationUtil.getLocalizedMessage(MessageKey.CITY_EMPTY));
                return new PagingResponse<>(cityResponseList, messages, 1,0L);
            }
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.CITY_GET_SUCCESS));
            return new PagingResponse<>(cityResponseList, messages, 1, (long) cityList.size());
        }else {
            cityRequest.setPage(Math.max(cityRequest.getPage(), 1));
            pageable = PageRequest.of(cityRequest.getPage() - 1, cityRequest.getLimit(), Sort.by("createdDate").descending());
        }
        Page<City> cityPage = iCityRepository.findAll(pageable);
        cityList = cityPage.getContent();
        cityResponseList = cityList.stream()
                .map(city -> modelMapper.map(city, CityResponse.class))
                .toList();
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.CITY_GET_SUCCESS));
        return new PagingResponse<>(cityResponseList, messages, cityPage.getTotalPages(), cityPage.getTotalElements());
    }

    @Override
    public APIResponse<Boolean> addCity(AddCityRequest addCityRequest) throws NotFoundException{
        if (iCityRepository.existsByName(addCityRequest.getName())){
            // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.CITY_EXISTED)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.CITY_EXISTED));
        }
        City city = modelMapper.map(addCityRequest, City.class);
        city.setName(addCityRequest.getName());
        String citySlug = vietnameseStringUtils.removeAccents(addCityRequest.getName()).trim().toLowerCase().replaceAll("\\s+", "-");
        city.setSlug(citySlug);
        city.setStatus(addCityRequest.isStatus());
        String countrySlug = StoreEnum.getCodeByCountry(addCityRequest.getCountry());
        city.setCountrySlug(countrySlug);
        iCityRepository.save(city);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.CITY_CREATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> editCity(String cityID, EditCityRequest editCityRequest) throws NotFoundException{
        City city = iCityRepository.findById(UUID.fromString(cityID)).orElse(null);
        if (city == null) {
            // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.CITY_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.CITY_NOT_FOUND));
        }

        modelMapper.map(editCityRequest, city);
        String citySlug = vietnameseStringUtils.removeAccents(editCityRequest.getCityName()).trim().toLowerCase().replaceAll("\\s+", "-");
        city.setSlug(citySlug);
        city.setStatus(editCityRequest.isStatus());
        String countrySlug = StoreEnum.getCodeByCountry(editCityRequest.getCityName());
        city.setCountrySlug(countrySlug);
        city.setUpdatedAt(LocalDateTime.now());
        iCityRepository.save(city);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.CITY_UPDATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> deleteCity(String cityID) throws NotFoundException {
        City city = iCityRepository.findById(UUID.fromString(cityID)).orElse(null);
        if (city == null) {
            // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.CITY_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.CITY_NOT_FOUND));
        }
        Media cityImage = city.getImage();
        if (cityImage != null && cityImage.getPublicId() != null) {
            cloudinaryService.deleteMedia(cityImage.getPublicId());
        }
        iCityRepository.delete(city);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.CITY_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> uploadImageCity(String cityID, MultipartFile image) throws NotFoundException {
        City city = iCityRepository.findById(UUID.fromString(cityID)).orElse(null);
        if (city == null) {
            // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.CITY_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.CITY_NOT_FOUND));
        }
        try {
            if (city.getImage() != null) {
                // If city already has an image, delete the old one
                cloudinaryService.deleteMedia(city.getImage().getPublicId());
                city.setImage(new Media()); // Clear the old image reference
                
            }
            Map uploadResult = cloudinaryService.uploadMedia(image, "cities");
            String thumbnailUrl = (String) uploadResult.get("secure_url");
            Media media = Media.builder()
                    .imageUrl(thumbnailUrl)
                    .publicId((String) uploadResult.get("public_id"))
                    .altText("Photo of the city: " + city.getName())
                    .referenceId(city.getId())
                    .referenceType("CITY")
                    .type("IMAGE")
                    .build();
            city.setImage(media);
            iCityRepository.save(city);
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.CITY_MEDIA_UPLOAD_SUCCESS));
            return new APIResponse<>(true, messages);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.CITY_MEDIA_UPLOAD_FAILED)));
        
        }
    }
}
