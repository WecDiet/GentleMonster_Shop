package com.gentlemonster.GentleMonsterBE.Controllers.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Story.AddStoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Story.EditStoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Story.StoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Story.StoryResponse;
import com.gentlemonster.GentleMonsterBE.Services.Story.StoryService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(Enpoint.Story.BASE)
public class StoryController {
    @Autowired
    private StoryService storyService;
    
    @GetMapping
    public ResponseEntity<PagingResponse<?>> getAllStories(StoryRequest storyRequest) {
        return ResponseEntity.ok(storyService.getAllStories(storyRequest));
    }

    @GetMapping(Enpoint.Story.ID)
    public ResponseEntity<APIResponse<StoryResponse>> getUserByID(@PathVariable String storyID) {
        return ResponseEntity.ok(storyService.getStoryById(storyID));
    }

    @PostMapping(Enpoint.Story.NEW)
    public ResponseEntity<APIResponse<Boolean>> addStory(@Valid @RequestBody AddStoryRequest addStoryRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(storyService.addStory(addStoryRequest));
    }

    @PutMapping(Enpoint.Story.ID)
    public ResponseEntity<APIResponse<Boolean>> editStory(@PathVariable String storyID, @Valid @RequestBody EditStoryRequest editStoryRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(storyService.editStory(storyID, editStoryRequest));
    }

    @DeleteMapping(Enpoint.Story.ID)
    public ResponseEntity<APIResponse<Boolean>> deleteStory(@PathVariable String storyID) {
        return ResponseEntity.ok(storyService.deleteStory(storyID));
    }

}
