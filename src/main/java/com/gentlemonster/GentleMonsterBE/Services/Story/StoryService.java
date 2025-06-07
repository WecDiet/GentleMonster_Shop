package com.gentlemonster.GentleMonsterBE.Services.Story;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Page;
import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Story.AddStoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Story.EditStoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Story.StoryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Story.BaseStoryResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Story.StoryResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Story.Public.BaseStoryPublicResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Story.Public.StoryPublicResponse;
import com.gentlemonster.GentleMonsterBE.Entities.Collaboration;
import com.gentlemonster.GentleMonsterBE.Entities.ProductType;
import com.gentlemonster.GentleMonsterBE.Entities.Slider;
import com.gentlemonster.GentleMonsterBE.Entities.Story;
import com.gentlemonster.GentleMonsterBE.Repositories.ICollaborationRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IProductTypeRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.ISliderRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IStoryRepository;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtil;
import com.gentlemonster.GentleMonsterBE.Utils.VietnameseStringUtils;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class StoryService implements IStoryService {

    @Autowired
    private IStoryRepository iStoryRepository;

    @Autowired
    private IProductTypeRepository iProductTypeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LocalizationUtil localizationUtil;

    @Autowired
    private VietnameseStringUtils vietnameseStringUtils;

    @Autowired
    private ICollaborationRepository iCollaborationRepository;

    @Autowired
    private ISliderRepository iSliderRepository;


    @Override
    public PagingResponse<List<BaseStoryResponse>> getAllStories(StoryRequest storyRequest) {
        List<BaseStoryResponse> BaseStoryReponsesList;
        List<Story> storyList;
        Pageable pageable;
        if (storyRequest.getLimit() == 0 && storyRequest.getPage() == 0) {
            storyList = iStoryRepository.findAll();
            BaseStoryReponsesList = storyList.stream()
                    .map(story -> modelMapper.map(story, BaseStoryResponse.class))
                    .toList();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_GET_SUCCESS));
            return new PagingResponse<>(BaseStoryReponsesList, messages, 1,(long) BaseStoryReponsesList.size());
        } else {
            storyRequest.setPage(Math.max(storyRequest.getPage(), 1));
            pageable = PageRequest.of(storyRequest.getPage(), storyRequest.getLimit());
        }

        Page<Story> storyPage = iStoryRepository.findAll(pageable);
        storyList = storyPage.getContent();
        BaseStoryReponsesList = storyList.stream()
                .map(story -> modelMapper.map(story, BaseStoryResponse.class))
                .toList();
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_GET_SUCCESS));
        return new PagingResponse<>(BaseStoryReponsesList, messages, storyPage.getTotalPages(), storyPage.getTotalElements());
    }

    @Override
    public APIResponse<StoryResponse> getStoryById(String storyID) {
        Story story = iStoryRepository.findById(UUID.fromString(storyID)).orElse(null);
        if (story == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_EMPTY));
            return new APIResponse<>(null, messages);
        }

        StoryResponse storyResponse = modelMapper.map(story, StoryResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_GET_SUCCESS));
        return new APIResponse<>(storyResponse, messages);
    }

    @Override
    public APIResponse<Boolean> addStory(@RequestBody AddStoryRequest addStoryRequest) {
        if (iStoryRepository.existsByName(addStoryRequest.getName())) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_EXISTED));
            return new APIResponse<>(false, messages);
        }
        Story story = modelMapper.map(addStoryRequest, Story.class);
        String storySlug = vietnameseStringUtils.removeAccents(addStoryRequest.getName());
        story.setSlug(storySlug);

        ProductType productType = iProductTypeRepository.findByName(addStoryRequest.getProductType()).orElse(null);
        if (productType == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
    
        Slider slider = iSliderRepository.findByName(addStoryRequest.getCollaboration()).orElse(null);
        if (slider == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        Collaboration collaboration = iCollaborationRepository.findBySlider(slider).orElse(null);
        if (collaboration == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        story.setCollaboration(collaboration);
        if (!addStoryRequest.getCollaboration().equalsIgnoreCase(collaboration.getSlider().getName())) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_INVALID));
            return new APIResponse<>(false, messages);
        }
        collaboration.setStory(story);
        story.setProductType(productType);
        iStoryRepository.save(story);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_CREATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> editStory(String storyID, EditStoryRequest editStoryRequest) {
        Story story = iStoryRepository.findById(UUID.fromString(storyID)).orElse(null);
        if (story == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        modelMapper.map(editStoryRequest, story);
        String storySlug = vietnameseStringUtils.removeAccents(editStoryRequest.getName());
        story.setSlug(storySlug);
        ProductType productType = iProductTypeRepository.findByName(editStoryRequest.getProductType()).orElse(null);
        if (productType == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        Slider slider = iSliderRepository.findByName(editStoryRequest.getCollaboration()).orElse(null);
        if (slider == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        Collaboration collaboration = iCollaborationRepository.findBySlider(slider).orElse(null);
        if (collaboration == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        story.setCollaboration(collaboration);
        if (!editStoryRequest.getCollaboration().equalsIgnoreCase(collaboration.getSlider().getName())) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_INVALID));
            return new APIResponse<>(false, messages);
        }
        collaboration.setStory(story);
        story.setProductType(productType);
        iStoryRepository.save(story);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_UPDATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> deleteStory(String storyID) {
        Story story = iStoryRepository.findById(UUID.fromString(storyID)).orElse(null);
        if (story == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        // Collaboration collaboration = iCollaborationRepository.findById(UUID.fromString(storyID)).orElse(null);
        // if (collaboration == null) {
        //     List<String> messages = new ArrayList<>();
        //     messages.add(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_NOT_FOUND));
        //     return new APIResponse<>(false, messages);
        // }
        iStoryRepository.delete(story);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<List<BaseStoryPublicResponse>> getAllStoriesPublic() {
        List<BaseStoryPublicResponse> baseStoryPublicResponsesList;
        List<Story> storyList;
        storyList = iStoryRepository.findAll();
        baseStoryPublicResponsesList = storyList.stream()
                .map(story -> modelMapper.map(story, BaseStoryPublicResponse.class))
                .toList();
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_GET_SUCCESS));
        return new APIResponse<>(baseStoryPublicResponsesList, messages);
    }

    @Override
    public APIResponse<StoryPublicResponse> getStoryBySlug(String slug) {
        Story story = iStoryRepository.findBySlug(slug).orElse(null);
        if (story == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_EMPTY));
            return new APIResponse<>(null, messages);
        }

        StoryPublicResponse storyResponse = modelMapper.map(story, StoryPublicResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_GET_SUCCESS));
        return new APIResponse<>(storyResponse, messages);
    }
}
