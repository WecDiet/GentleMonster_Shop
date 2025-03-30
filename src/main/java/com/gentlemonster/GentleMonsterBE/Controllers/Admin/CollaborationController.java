package com.gentlemonster.GentleMonsterBE.Controllers.Admin;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Collaboration.CollaborationRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Collaboration.EditCollaborationRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.Services.Collaboration.CollaborationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Enpoint.Collaboration.BASEADMIN)
public class CollaborationController {

    @Autowired
    private CollaborationService collaborationService;

    @GetMapping
    public ResponseEntity<PagingResponse<?>> getAllCollaboration(@ModelAttribute CollaborationRequest collaborationRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new PagingResponse<>(null,  errorMessages, 0, (long) 0));
        }
        return ResponseEntity.ok(collaborationService.getAllCollaboration(collaborationRequest));
    }

    @GetMapping(Enpoint.Collaboration.ID)
    public ResponseEntity<APIResponse<?>> getOneCollaboration(@PathVariable String collaborationID) {
        return ResponseEntity.ok(collaborationService.getOneCollaboration(collaborationID));
    }

    @PutMapping(Enpoint.Collaboration.EDIT)
    public ResponseEntity<APIResponse<Boolean>> editCollaboration(@PathVariable String collaborationID, @RequestBody EditCollaborationRequest editCollaborationRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new APIResponse<>(false,  errorMessages));
        }
        return ResponseEntity.ok(collaborationService.editCollaboration(collaborationID, editCollaborationRequest));
    }
}
