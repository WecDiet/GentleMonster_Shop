package com.gentlemonster.GentleMonsterBE.Controllers.Public;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Story.Public.BaseStoryPublicResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Story.Public.StoryPublicResponse;
import com.gentlemonster.GentleMonsterBE.Services.Story.StoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping
public class StoryPublicController {
    @Autowired
    private StoryService storyService;

    @GetMapping(Enpoint.Story.PUBLIC_STORY)
    public ResponseEntity<APIResponse<List<BaseStoryPublicResponse>>> getAllStoriesPublic() {
        return ResponseEntity.ok(storyService.getAllStoriesPublic());
    }
    
    @GetMapping(Enpoint.Story.PUBLIC_STORY_DETAIL)
    public ResponseEntity<APIResponse<StoryPublicResponse>> getStoryBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(storyService.getStoryBySlug(slug));
    }
}
