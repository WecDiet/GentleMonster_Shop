package com.gentlemonster.GentleMonsterBE.Services.Collaboration;


import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Collaboration.CollaborationRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Collaboration.EditCollaborationRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Collaboration.CollaborationResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.Entities.Collaboration;
import com.gentlemonster.GentleMonsterBE.Repositories.ICollaborationRepository;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtil;
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
public class CollaborationService implements ICollaborationService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LocalizationUtil localizationUtil;
    @Autowired
    private ICollaborationRepository ICollaborationRepository;


//    @Override
//    public PagingResponse<List<CollaborationResponse>> getAllCollaboration(CollaborationRequest collaborationRequest) {
//        List<CollaborationResponse> collaborationResponseList;
//        List<Collaboration> collaborationList;
//        Pageable pageable;
//
//        int page = Math.max(collaborationRequest.getPage() - 1, 0); // Page index should start from 0
//        int size = collaborationRequest.getLimit() > 0 ? collaborationRequest.getLimit() : 10; // Default size is 10 if limit is not provided
//        pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
//
//        Page<Collaboration> collaborationPage = ICollaborationRepository.findAll(pageable);
//        collaborationList = collaborationPage.getContent();
//        collaborationResponseList = collaborationList.stream()
//                .map(collaboration -> modelMapper.map(collaboration, CollaborationResponse.class))
//                .toList();
//        if (collaborationResponseList.isEmpty()) {
//            return new PagingResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_EMPTY)), 0, 0L);
//        }
//
//        ArrayList<String> messages = new ArrayList<>();
//        messages.add(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_GET_SUCCESS));
//        return new PagingResponse<>(collaborationResponseList, messages, collaborationPage.getTotalPages(), collaborationPage.getTotalElements());
//    }

    @Override
    public PagingResponse<List<CollaborationResponse>> getAllCollaboration(CollaborationRequest collaborationRequest) {
        List<CollaborationResponse> collaborationResponseList;
        List<Collaboration> collaborationList;
        Pageable pageable;
        if(collaborationRequest.getPage() == 0 && collaborationRequest.getLimit() == 0){
            collaborationList = ICollaborationRepository.findAll();
            collaborationResponseList = collaborationList.stream()
                    .map(collaboration -> modelMapper.map(collaboration, CollaborationResponse.class))
                    .toList();
            if (collaborationResponseList.isEmpty()) {
                return new PagingResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_EMPTY)), 0, 0L);
            }
            ArrayList<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_GET_SUCCESS));
            return new PagingResponse<>(collaborationResponseList, messages, 1, (long) collaborationResponseList.size());
        }else {
            collaborationRequest.setPage(Math.max(collaborationRequest.getPage(), 1));
            pageable = PageRequest.of(collaborationRequest.getPage() - 1, collaborationRequest.getLimit(), Sort.by("createdDate").descending());
        }

        Page<Collaboration> collaborationPage = ICollaborationRepository.findAll(pageable);
        collaborationList = collaborationPage.getContent();
        collaborationResponseList = collaborationList.stream()
                .map(collaboration -> modelMapper.map(collaboration, CollaborationResponse.class))
                .toList();
        if (collaborationResponseList.isEmpty()) {
            return new PagingResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_EMPTY)), 0, 0L);
        }

        ArrayList<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_GET_SUCCESS));
        return new PagingResponse<>(collaborationResponseList, messages, collaborationPage.getTotalPages(), collaborationPage.getTotalElements());
    }

    @Override
    public APIResponse<Boolean> editCollaboration(String collaborationID,EditCollaborationRequest editCollaborationRequest) {
        Collaboration collaboration = ICollaborationRepository.findById(UUID.fromString(collaborationID)).orElse(null);
        if (collaboration == null) {
            return new APIResponse<>(false, List.of(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_NOT_FOUND)));
        }
        collaboration.setStatus(editCollaborationRequest.isStatus());
        collaboration.setModifiedDate(LocalDateTime.now());
        ICollaborationRepository.save(collaboration);

        ArrayList<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_UPDATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<CollaborationResponse> getOneCollaboration(String collaborationID) {
        Collaboration collaboration = ICollaborationRepository.findById(UUID.fromString(collaborationID)).orElse(null);
        if (collaboration == null) {
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_NOT_FOUND)));
        }
        CollaborationResponse collaborationResponse = modelMapper.map(collaboration, CollaborationResponse.class);
        ArrayList<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_GET_SUCCESS));
        return new APIResponse<>(collaborationResponse, messages);
    }
}
