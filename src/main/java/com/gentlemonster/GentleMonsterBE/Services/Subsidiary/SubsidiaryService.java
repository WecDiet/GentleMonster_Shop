package com.gentlemonster.GentleMonsterBE.Services.Subsidiary;

import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Subsidiary.AddSubsidiaryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Subsidiary.EditSubsidiaryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Subsidiary.SubsidiaryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Subsidiary.BaseSubsidiaryResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Subsidiary.SubsidiaryResponse;
import com.gentlemonster.GentleMonsterBE.Entities.City;
import com.gentlemonster.GentleMonsterBE.Entities.Subsidiary;
import com.gentlemonster.GentleMonsterBE.Repositories.ICityRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.ISubsidiaryRepository;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtil;
import com.gentlemonster.GentleMonsterBE.Utils.VietnameseStringUtils;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@NoArgsConstructor
public class SubsidiaryService implements ISubsidiaryService {
    @Autowired
    private ISubsidiaryRepository iSubsidiaryRepository;
    @Autowired
    private ICityRepository iCityRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LocalizationUtil localizationUtil;
    @Autowired
    private VietnameseStringUtils vietnameseStringUtils;

    @Override
    public PagingResponse<List<BaseSubsidiaryResponse>> getAllSubsidiary(SubsidiaryRequest subsidiaryRequest) {
        List<BaseSubsidiaryResponse> baseSubsidiaryResponseList;
        List<Subsidiary> subsidiaryList;
        Pageable pageable;
        if (subsidiaryRequest.getPage() == 0 && subsidiaryRequest.getLimit() == 0){
            subsidiaryList = iSubsidiaryRepository.findAll();
            baseSubsidiaryResponseList = subsidiaryList.stream()
                    .map(subsidiary -> modelMapper.map(subsidiary, BaseSubsidiaryResponse.class))
                    .toList();
            if (baseSubsidiaryResponseList.isEmpty()){
                List<String> messages = new ArrayList<>();
                messages.add(localizationUtil.getLocalizedMessage(MessageKey.SUBSIDIARY_EMPTY));
                return new PagingResponse<>(baseSubsidiaryResponseList, messages, 1,0L);
            }
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.SUBSIDIARY_GET_SUCCESS));
            return new PagingResponse<>(baseSubsidiaryResponseList, messages, 1, (long) subsidiaryList.size());
        }else {
            subsidiaryRequest.setPage(Math.max(subsidiaryRequest.getPage(), 1));
            pageable = PageRequest.of(subsidiaryRequest.getPage() - 1, subsidiaryRequest.getLimit(), Sort.by("createdDate").descending());
        }

        Page<Subsidiary> subsidiaryPage = iSubsidiaryRepository.findAll(pageable);
        subsidiaryList = subsidiaryPage.getContent();
        baseSubsidiaryResponseList = subsidiaryList.stream()
                .map(subsidiary -> modelMapper.map(subsidiary, BaseSubsidiaryResponse.class))
                .toList();
        if (baseSubsidiaryResponseList.isEmpty()){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.SUBSIDIARY_EMPTY));
            return new PagingResponse<>(baseSubsidiaryResponseList, messages, 1,0L);

        }
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.SUBSIDIARY_GET_SUCCESS));
        return new PagingResponse<>(baseSubsidiaryResponseList, messages, subsidiaryPage.getTotalPages(), subsidiaryPage.getTotalElements());
    }

    @Override
    public APIResponse<SubsidiaryResponse> getSubsidiaryByID(String subsidiaryID) {
        Subsidiary subsidiary = iSubsidiaryRepository.findById(UUID.fromString(subsidiaryID)).orElse(null);
        if (subsidiary == null) {
            return new APIResponse<>(null,List.of(localizationUtil.getLocalizedMessage(MessageKey.SUBSIDIARY_NOT_FOUND)));
        }
        SubsidiaryResponse subsidiaryResponse = modelMapper.map(subsidiary, SubsidiaryResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.SUBSIDIARY_GET_SUCCESS));
        return new APIResponse<>(subsidiaryResponse, messages);
    }

    @Override
    public APIResponse<Boolean> addSubsidiary(AddSubsidiaryRequest addSubsidiaryRequest) {
        if (iSubsidiaryRepository.existsByCompanyName(addSubsidiaryRequest.getCompanyName())){
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.SUBSIDIARY_EXISTED)));
        }
        Subsidiary subsidiary = modelMapper.map(addSubsidiaryRequest, Subsidiary.class);
        String subsidiarySlug = vietnameseStringUtils.removeAccents(addSubsidiaryRequest.getCompanyName()).trim().toLowerCase().replaceAll("\\s+", "-");
        subsidiary.setSlug(subsidiarySlug);
        subsidiary.setStatus(addSubsidiaryRequest.isStatus());
        City city = iCityRepository.findByName(addSubsidiaryRequest.getCity()).orElse(null);
        if (city == null) {
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.CITY_NOT_FOUND)));
        }
        subsidiary.setCity(city);
        iSubsidiaryRepository.save(subsidiary);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.SUBSIDIARY_CREATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> editSubsidiary(String subsidiaryID, EditSubsidiaryRequest editSubsidiaryRequest) {
        Subsidiary subsidiary = iSubsidiaryRepository.findById(UUID.fromString(subsidiaryID)).orElse(null);
        if (subsidiary == null) {
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.SUBSIDIARY_NOT_FOUND)));
        }
        modelMapper.map(editSubsidiaryRequest, subsidiary);
        String subsidiarySlug = vietnameseStringUtils.removeAccents(editSubsidiaryRequest.getCompanyName()).trim().toLowerCase().replaceAll("\\s+", "-");
        subsidiary.setSlug(subsidiarySlug);
        City city = iCityRepository.findByName(editSubsidiaryRequest.getCity()).orElse(null);
        if (city == null) {
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.CITY_NOT_FOUND)));
        }
        subsidiary.setStatus(editSubsidiaryRequest.isStatus());
        subsidiary.setUpdatedAt(LocalDateTime.now());
        subsidiary.setCity(city);
        iSubsidiaryRepository.save(subsidiary);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.SUBSIDIARY_UPDATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> deleteSubsidiary(String subsidiaryID) {
        Subsidiary subsidiary = iSubsidiaryRepository.findById(UUID.fromString(subsidiaryID)).orElse(null);
        if (subsidiary == null) {
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.SUBSIDIARY_NOT_FOUND)));
        }
        iSubsidiaryRepository.delete(subsidiary);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.SUBSIDIARY_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }
}
