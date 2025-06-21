package com.gentlemonster.GentleMonsterBE.Services.Store;

import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Store.AddStoreRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Store.EditStoreRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Store.StoreRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Store.BaseStoreResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Store.StorePublicResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Store.StoreResponse;
import com.gentlemonster.GentleMonsterBE.Entities.Category;
import com.gentlemonster.GentleMonsterBE.Entities.City;
import com.gentlemonster.GentleMonsterBE.Entities.Media;
import com.gentlemonster.GentleMonsterBE.Entities.Slider;
import com.gentlemonster.GentleMonsterBE.Entities.Store;
import com.gentlemonster.GentleMonsterBE.Repositories.ICategoryRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.ICityRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.ISliderRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IStoreRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.Specification.StoreSpecification;
import com.gentlemonster.GentleMonsterBE.Services.Cloudinary.CloudinaryService;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtil;
import com.gentlemonster.GentleMonsterBE.Utils.VietnameseStringUtils;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class StoreService implements IStoreService{

    @Autowired
    private IStoreRepository iStoreRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LocalizationUtil localizationUtil;
    @Autowired
    private VietnameseStringUtils vietnameseStringUtils;
    @Autowired
    private ISliderRepository iSliderRepository;
    @Autowired
    private ICityRepository iCityRepository;
    @Autowired
    private ICategoryRepository iCategoryRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public PagingResponse<List<BaseStoreResponse>> GetAllStore(StoreRequest storeRequest) {
        List<BaseStoreResponse> baseStoreResponses;
        List<Store> listStore;
        Pageable pageable;
        if (storeRequest.getPage() == 0 && storeRequest.getLimit() == 0) {
            listStore = iStoreRepository.findAll();
            baseStoreResponses = listStore.stream()
                    .map(store -> modelMapper.map(store, BaseStoreResponse.class))
                    .toList();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_GET_SUCCESS));
            return new PagingResponse<>(baseStoreResponses, messages, 1,(long) baseStoreResponses.size());
        }else{
            storeRequest.setPage(Math.max(storeRequest.getPage(), 1));
            pageable = PageRequest.of(storeRequest.getPage(), storeRequest.getLimit());
        }
        Page<Store> pageStore = iStoreRepository.findAll(pageable);
        baseStoreResponses = pageStore.getContent().stream()
                .map(store -> modelMapper.map(store, BaseStoreResponse.class))
                .toList();
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_GET_SUCCESS));
        return new PagingResponse<>(baseStoreResponses, messages, pageStore.getNumber(), pageStore.getTotalElements());
    }

    @Override
    public APIResponse<Boolean> AddStore(AddStoreRequest addStoreRequest) {
        if (iStoreRepository.existsByStoreName(addStoreRequest.getStoreName())) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_EXISTED));
            return new APIResponse<>(false, messages);
        }
        Store store = modelMapper.map(addStoreRequest, Store.class);
        City city = iCityRepository.findByName(addStoreRequest.getCity()).orElse(null);
        if (city == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.CITY_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        store.setCity(city);
        Category category = iCategoryRepository.findByName("Stores").orElse(null);
        if (category == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        String sliderName = city.getName();
        String sliderSlug = vietnameseStringUtils.removeAccents(city.getName());
        Slider slider = iSliderRepository.findByName(sliderName).orElse(null);
        if (slider == null) {
            // Nếu chưa có thì tạo mới
            slider = Slider.builder()
                    .name(sliderName)
                    .status(city.isStatus())
                    .slug(sliderSlug)
                    .category(category)
                    .build();
            slider = iSliderRepository.save(slider);
        }
        store.setSlider(slider);
        iStoreRepository.save(store);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_CREATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> EditStore(String storeID, EditStoreRequest editStoreRequest) {
        return null;
    }

    @Override
    public APIResponse<Boolean> DeleteStore(String storeID) {
        Store store = iStoreRepository.findById(UUID.fromString(storeID)).orElse(null);
        if (store == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }
        Media storeThumb = store.getThumbnailMedia();
        if (storeThumb != null && storeThumb.getPublicId() != null) {
            // Xóa media thumbnail nếu có
            cloudinaryService.deleteMedia(storeThumb.getPublicId());
        }

        if (store.getImages() != null) {
            store.getImages().stream()
                .filter(media -> media != null && media.getPublicId() != null)
                .map(Media::getPublicId)
                .forEach(cloudinaryService::deleteMedia);
        }
        iStoreRepository.delete(store);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<StoreResponse> GetOneStore(String storeID) {
        Store store = iStoreRepository.findById(UUID.fromString(storeID)).orElse(null);
        if (store == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_NOT_FOUND));
            return new APIResponse<>(null, messages);
        }
        StoreResponse storeResponse = modelMapper.map(store, StoreResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_GET_SUCCESS));
        return new APIResponse<>(storeResponse, messages);
    }

    @Override
    public APIResponse<List<StorePublicResponse>> GetAllStoreByCountry(StoreRequest storeRequest) {
        List<StorePublicResponse> storePublicResponses;
        List<Store> listStore;
        if (!iCityRepository.existsByCountrySlug(storeRequest.getCountry())){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.COUNTRY_NOT_FOUND));
            return new APIResponse<>(null, messages);
        }
        if (storeRequest.getCity() != null && !storeRequest.getCity().isEmpty()) {
            if (!iCityRepository.existsByName(storeRequest.getCity())) {
                List<String> messages = new ArrayList<>();
                messages.add(localizationUtil.getLocalizedMessage(MessageKey.CITY_NOT_FOUND));
                return new APIResponse<>(null, messages);
            }
        }
        Specification<Store> specification = StoreSpecification.filterStore(storeRequest.getCountry(), storeRequest.getCity());
        listStore = iStoreRepository.findAll(specification);
        storePublicResponses = listStore.stream()
                .map(store -> modelMapper.map(store, StorePublicResponse.class))
                .toList();
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_GET_SUCCESS));
        return new APIResponse<>(storePublicResponses, messages);
    }

    @Override
    public APIResponse<Boolean> uploadMedia(String storeID, MultipartFile[] images, String type) {
        try {
            if ("THUMBNAIL".equalsIgnoreCase(type)) {
                return handleUploadThumbnail(storeID, images[0]);
            } else if ("GALLERY".equalsIgnoreCase(type)) {
                return handleUploadGallery(storeID, images);
            } else {
                List<String> messages = new ArrayList<>();
                messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_NOT_VALID_FILES_UPLOADED));
                return new APIResponse<>(false, messages);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_UPLOAD_MEDIA_FAILED));
            return new APIResponse<>(false, messages);
        }
    }

    private APIResponse<Boolean> handleUploadThumbnail(String storeID, MultipartFile file) {
        Store store = iStoreRepository.findById(UUID.fromString(storeID)).orElse(null);
        if (store == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }

        try {
            if (store.getThumbnailMedia() != null) {
                // Xóa media cũ nếu có
                cloudinaryService.deleteMedia(store.getThumbnailMedia().getPublicId());
                store.setThumbnailMedia(new Media());
                
            }
            Map uploadResult = cloudinaryService.uploadMedia(file, "stores");
            String imageURL = (String) uploadResult.get("secure_url");

            Media thumbnailMedia = Media.builder()
                    .imageUrl(imageURL)
                    .publicId((String) uploadResult.get("public_id"))
                    .referenceId(store.getId())
                    .referenceType("STORE")
                    .altText("Thumbnail for store: " + store.getStoreName())
                    .type("THUMBNAIL")
                    .build();

            store.setThumbnailMedia(thumbnailMedia);
            iStoreRepository.save(store);

            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_UPLOAD_THUMBNAIL_SUCCESS));
            return new APIResponse<>(true, messages);
        } catch (Exception e) {
            e.printStackTrace();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_UPLOAD_THUMBNAIL_FAILED));
            return new APIResponse<>(false, messages);
        }
    }

    private APIResponse<Boolean> handleUploadGallery(String storeID, MultipartFile[] images) {
        Store store = iStoreRepository.findById(UUID.fromString(storeID)).orElse(null);
        if (store == null) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_NOT_FOUND));
            return new APIResponse<>(false, messages);
        }

        try {
            store.getImages().stream()
                .filter(media -> "GALLERY".equals(media.getType()) && media.getPublicId() != null)
                .forEach(media -> {
                    cloudinaryService.deleteMedia(media.getPublicId());
                });
            store.getImages().clear();
            List<Media> newImages = Arrays.stream(images)
                .filter(image -> image != null && !image.isEmpty())
                .map(
                    image -> {
                        String originalFilename = image.getOriginalFilename();
                        if (originalFilename == null || !originalFilename.contains(".")) {
                            throw new RuntimeException("Invalid file name: " + (originalFilename != null ? originalFilename : "null"));
                        }
                        Map uploadResult = cloudinaryService.uploadMedia(image, "stores");
                        String imageURL = (String) uploadResult.get("secure_url");
                        return Media.builder()
                                .imageUrl(imageURL)
                                .publicId((String) uploadResult.get("public_id"))
                                .referenceId(store.getId())
                                .referenceType("STORE")
                                .altText("Gallery image for store: " + store.getStoreName())
                                .type("GALLERY")
                                .build();
                    }
                ).collect(Collectors.toList());
            store.getImages().addAll(newImages);
            iStoreRepository.save(store);

            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_UPLOAD_MEDIA_SUCCESS));
            return new APIResponse<>(true, messages);
        } catch (Exception e) {
            e.printStackTrace();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_UPLOAD_MEDIA_FAILED));
            return new APIResponse<>(false, messages);
        }
    }

    
}
