package com.gentlemonster.Services.Collaboration;

import com.gentlemonster.DTO.Requests.Collaboration.CollaborationRequest;
import com.gentlemonster.DTO.Requests.Collaboration.EditCollaborationRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.Collaboration.CollaborationResponse;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.DTO.Responses.PagingResponse;

import java.util.List;

public interface ICollaborationService {
    PagingResponse<List<CollaborationResponse>> getAllCollaboration(CollaborationRequest collaborationRequest);
    APIResponse<Boolean> editCollaboration(String collaborationID,EditCollaborationRequest editCollaborationRequest) throws NotFoundException;
    APIResponse<CollaborationResponse> getOneCollaboration(String collaborationID) throws NotFoundException;
    
}
