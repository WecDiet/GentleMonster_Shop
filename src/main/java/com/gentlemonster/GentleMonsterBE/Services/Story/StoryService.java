package com.gentlemonster.GentleMonsterBE.Services.Story;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
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
import com.gentlemonster.GentleMonsterBE.Entities.Media;
import com.gentlemonster.GentleMonsterBE.Entities.ProductType;
import com.gentlemonster.GentleMonsterBE.Entities.Slider;
import com.gentlemonster.GentleMonsterBE.Entities.Story;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;
import com.gentlemonster.GentleMonsterBE.Repositories.ICollaborationRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IProductTypeRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.ISliderRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IStoryRepository;
import com.gentlemonster.GentleMonsterBE.Services.Cloudinary.CloudinaryService;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtils;
import com.gentlemonster.GentleMonsterBE.Utils.ValidationUtils;

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
    private LocalizationUtils localizationUtil;

    @Autowired
    private ValidationUtils vietnameseStringUtils;

    @Autowired
    private ICollaborationRepository iCollaborationRepository;

    @Autowired
    private ISliderRepository iSliderRepository;

    @Autowired
    private CloudinaryService cloudinaryService;


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
    public APIResponse<StoryResponse> getStoryById(String storyID) throws NotFoundException{
        Story story = iStoryRepository.findById(UUID.fromString(storyID)).orElse(null);
        if (story == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_EMPTY));
            // return new APIResponse<>(null, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.STORY_NOT_FOUND));
        }

        StoryResponse storyResponse = modelMapper.map(story, StoryResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_GET_SUCCESS));
        return new APIResponse<>(storyResponse, messages);
    }

    @Override
    public APIResponse<Boolean> addStory(@RequestBody AddStoryRequest addStoryRequest) throws NotFoundException {
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
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND));
        }
    
        Slider slider = iSliderRepository.findByName(addStoryRequest.getCollaboration()).orElse(null);
        if (slider == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
        }
        Collaboration collaboration = iCollaborationRepository.findBySlider(slider).orElse(null);
        if (collaboration == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_NOT_FOUND));
        }
        if (!iStoryRepository.existsByName(addStoryRequest.getCollaboration())) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_EXISTED));
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
    public APIResponse<Boolean> editStory(String storyID, EditStoryRequest editStoryRequest) throws NotFoundException {
        Story story = iStoryRepository.findById(UUID.fromString(storyID)).orElse(null);
        if (story == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.STORY_NOT_FOUND));
        }
        modelMapper.map(editStoryRequest, story);
        String storySlug = vietnameseStringUtils.removeAccents(editStoryRequest.getName());
        story.setSlug(storySlug);
        ProductType productType = iProductTypeRepository.findByName(editStoryRequest.getProductType()).orElse(null);
        if (productType == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.PRODUCT_TYPE_NOT_FOUND));
        }
        Slider slider = iSliderRepository.findByName(editStoryRequest.getCollaboration()).orElse(null);
        if (slider == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.SLIDER_NOT_FOUND));
        }
        Collaboration collaboration = iCollaborationRepository.findBySlider(slider).orElse(null);
        if (collaboration == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.COLLABORATION_NOT_FOUND));
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
    public APIResponse<Boolean> deleteStory(String storyID) throws NotFoundException {
        Story story = iStoryRepository.findById(UUID.fromString(storyID)).orElse(null);
        if (story == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.STORY_NOT_FOUND));
        }
        // Media storyThumb = story.getThumbnailMedia();
        // if (storyThumb != null && storyThumb.getPublicId() != null) {
        //     // Xóa media thumbnail nếu có
        //     cloudinaryService.deleteMedia(storyThumb.getPublicId());
        // }
        if (story.getImage() != null) {
                story.getImage().stream()
                    .filter(media -> media != null && media.getPublicId() != null)
                    .map(Media::getPublicId)
                    .forEach(cloudinaryService::deleteMedia);
        }
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
    public APIResponse<StoryPublicResponse> getStoryBySlug(String slug) throws NotFoundException {
        Story story = iStoryRepository.findBySlug(slug).orElse(null);
        if (story == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_EMPTY));
            // return new APIResponse<>(null, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.STORY_NOT_FOUND));
        }

        StoryPublicResponse storyResponse = modelMapper.map(story, StoryPublicResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_GET_SUCCESS));
        return new APIResponse<>(storyResponse, messages);
    }

    // @Override
    // public APIResponse<Boolean> uploadMedia(String storyId, MultipartFile[] images, String type) {
        
    //     try {
    //         if ("THUMBNAIL".equalsIgnoreCase(type)) {
    //             return handleUploadThumbnailStory(storyId, images[0]);
    //         } else if ("GALLERY".equalsIgnoreCase(type)) {
    //             return handleUploadGalleryStory(storyId, images);
    //         }else {
    //             List<String> messages = new ArrayList<>();
    //             messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_NOT_VALID_FILES_UPLOADED));
    //             return new APIResponse<>(false, messages);
                
    //         }
    //     } catch (Exception e) {
    //         List<String> messages = new ArrayList<>();
    //         messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_UPLOAD_MEDIA_FAILED));
    //         return new APIResponse<>(false, messages);
    //     }
    // }


    // private APIResponse<Boolean> handleUploadThumbnailStory(String storyId, MultipartFile image) throws NotFoundException {
    //     Story story = iStoryRepository.findById(UUID.fromString(storyId)).orElse(null);
    //     if (story == null) {
    //         throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.STORY_NOT_FOUND));
    //     }
    //     try {
    //         if (story.getThumbnailMedia() != null) {
    //             // If the story already has a thumbnail, delete it first
    //             cloudinaryService.deleteMedia(story.getThumbnailMedia().getPublicId());
    //             story.setThumbnailMedia(new Media());      
    //         }
    //         Map uploadResult = cloudinaryService.uploadMedia(image, "stories");
    //         String imageURL = (String) uploadResult.get("secure_url");
    //         Media thumbnailMedia = Media.builder()
    //                 .imageUrl(imageURL)
    //                 .publicId((String) uploadResult.get("public_id"))
    //                 .altText("Thumbnail for story: " + story.getName())
    //                 .referenceId(story.getId())
    //                 .referenceType("STORY")
    //                 .type("THUMBNAIL")
    //                 .build();
    //         story.setThumbnailMedia(thumbnailMedia);
    //         iStoryRepository.save(story);
    //         List<String> messages = new ArrayList<>();
    //         messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_UPLOAD_THUMBNAIL_SUCCESS));
    //         return new APIResponse<>(true, messages);
    //     } catch (Exception e) {
    //         List<String> messages = new ArrayList<>();
    //         messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_UPLOAD_THUMBNAIL_FAILED));
    //         return new APIResponse<>(false, messages);
    //     }
    // }

    @Override
    public APIResponse<Boolean> handleUploadGalleryStory(String storyId, MultipartFile[] images) throws NotFoundException {
        Story story = iStoryRepository.findById(UUID.fromString(storyId)).orElse(null);
        if (story == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.STORY_NOT_FOUND));
        }
        try {
            story.getImage().stream()
                .filter(media -> "GALLERY".equals(media.getType()) && media.getPublicId() != null)
                .forEach(media -> {
                    // Delete existing gallery images from Cloudinary
                    cloudinaryService.deleteMedia(media.getPublicId());
                });
            story.getImage().clear(); // Clear existing gallery images
            List<Media> newImages = Arrays.stream(images)
                .filter(image -> !image.isEmpty() && image != null)
                .map(image -> {
                    String originalFilename = image.getOriginalFilename();
                    if (originalFilename == null || !originalFilename.contains(".")) {
                        throw new IllegalArgumentException("Invalid image file name: " + originalFilename);
                    }

                    Map uploadResult = cloudinaryService.uploadMedia(image, "stories");
                    String imageURL = (String) uploadResult.get("secure_url");
                    return Media.builder()
                            .imageUrl(imageURL)
                            .publicId((String) uploadResult.get("public_id"))
                            .altText("Gallery image for story: " + story.getName())
                            .referenceId(story.getId())
                            .referenceType("STORY")
                            .type("GALLERY")
                            .build();
                }
            ).collect(Collectors.toList());
            story.getImage().addAll(newImages);
            iStoryRepository.save(story);

            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_UPLOAD_MEDIA_SUCCESS));
            return new APIResponse<>(true, messages);
        } catch (Exception e) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORY_UPLOAD_MEDIA_FAILED));
            return new APIResponse<>(false, messages);
        }
    }
}
