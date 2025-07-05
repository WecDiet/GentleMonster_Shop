package com.gentlemonster.GentleMonsterBE.Services.Story;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Story.AddStoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Story.EditStoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Story.StoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Story.BaseStoryResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Story.StoryResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Story.Public.BaseStoryPublicResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Story.Public.StoryPublicResponse;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;

public interface IStoryService {
    // Define methods for the story service here, e.g.:
    PagingResponse<List<BaseStoryResponse>> getAllStories(StoryRequest storyRequest);
    APIResponse<StoryResponse> getStoryById(String storyID) throws NotFoundException;
    APIResponse<Boolean> addStory(AddStoryRequest addStoryRequest) throws NotFoundException;
    APIResponse<Boolean> editStory(String storyId, EditStoryRequest editStoryRequest) throws NotFoundException;
    APIResponse<Boolean> deleteStory(String storyId) throws NotFoundException;
    APIResponse<List<BaseStoryPublicResponse>> getAllStoriesPublic();
    APIResponse<StoryPublicResponse> getStoryBySlug(String slug) throws NotFoundException;
    APIResponse<Boolean> handleUploadGalleryStory(String storyId, MultipartFile[] images) throws NotFoundException;
}