package com.gentlemonster.GentleMonsterBE.Services.Collaboration;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Collaboration.CollaborationRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Collaboration.EditCollaborationRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Collaboration.CollaborationResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;

import java.util.List;

public interface ICollaborationService {
    PagingResponse<List<CollaborationResponse>> getAllCollaboration(CollaborationRequest collaborationRequest);
    APIResponse<Boolean> editCollaboration(String collaborationID,EditCollaborationRequest editCollaborationRequest);
    APIResponse<CollaborationResponse> getOneCollaboration(String collaborationID);
}
