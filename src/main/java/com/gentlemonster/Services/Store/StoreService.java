package com.gentlemonster.Services.Store;

import com.gentlemonster.Contants.MessageKey;
import com.gentlemonster.DTO.Requests.Store.AddStoreRequest;
import com.gentlemonster.DTO.Requests.Store.EditStoreRequest;
import com.gentlemonster.DTO.Requests.Store.StoreRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.Store.BaseStoreResponse;
import com.gentlemonster.DTO.Responses.Store.StorePublicResponse;
import com.gentlemonster.DTO.Responses.Store.StoreResponse;
import com.gentlemonster.Entities.Category;
import com.gentlemonster.Entities.City;
import com.gentlemonster.Entities.Media;
import com.gentlemonster.Entities.Slider;
import com.gentlemonster.Entities.Store;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Repositories.ICategoryRepository;
import com.gentlemonster.Repositories.ICityRepository;
import com.gentlemonster.Repositories.ISliderRepository;
import com.gentlemonster.Repositories.IStoreRepository;
import com.gentlemonster.Repositories.Specification.StoreSpecification;
import com.gentlemonster.Services.Cloudinary.CloudinaryService;
import com.gentlemonster.Utils.LocalizationUtils;
import com.gentlemonster.Utils.ValidationUtils;
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
    private LocalizationUtils localizationUtil;
    @Autowired
    private ValidationUtils vietnameseStringUtils;
    @Autowired
    private ISliderRepository iSliderRepository;
    @Autowired
    private ICityRepository iCityRepository;
    @Autowired
    private ICategoryRepository iCategoryRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public PagingResponse<List<BaseStoreResponse>> getAllStore(StoreRequest storeRequest) {
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
    public APIResponse<Boolean> addStore(AddStoreRequest addStoreRequest) throws NotFoundException{
        if (iStoreRepository.existsByStoreName(addStoreRequest.getStoreName())) {
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_EXISTED));
            return new APIResponse<>(false, messages);
        }
        Store store = modelMapper.map(addStoreRequest, Store.class);
        City city = iCityRepository.findByName(addStoreRequest.getCity()).orElse(null);
        if (city == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.CITY_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.CITY_NOT_FOUND));
        }
        store.setCity(city);
        Category category = iCategoryRepository.findByName("Stores").orElse(null);
        if (category == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND));
            // return new APIResponse<>(false, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.CATEGORY_NOT_FOUND));
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
    public APIResponse<Boolean> editStore(String storeID, EditStoreRequest editStoreRequest) throws NotFoundException {
        return null;
    }

    @Override
    public APIResponse<Boolean> deleteStore(String storeID) throws NotFoundException {
        Store store = iStoreRepository.findById(UUID.fromString(storeID)).orElse(null);
        if (store == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.STORE_NOT_FOUND));
        }
        // Media storeThumb = store.getThumbnailMedia();
        // if (storeThumb != null && storeThumb.getPublicId() != null) {
        //     // Xóa media thumbnail nếu có
        //     cloudinaryService.deleteMedia(storeThumb.getPublicId());
        // }

        if (store.getImage() != null) {
            store.getImage().stream()
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
    public APIResponse<StoreResponse> getOneStore(String storeID) throws NotFoundException {
        Store store = iStoreRepository.findById(UUID.fromString(storeID)).orElse(null);
        if (store == null) {
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_NOT_FOUND));
            // return new APIResponse<>(null, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.STORE_NOT_FOUND));
        }
        StoreResponse storeResponse = modelMapper.map(store, StoreResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_GET_SUCCESS));
        return new APIResponse<>(storeResponse, messages);
    }

    @Override
    public APIResponse<List<StorePublicResponse>> getAllStoreByCountry(StoreRequest storeRequest) throws NotFoundException {
        List<StorePublicResponse> storePublicResponses;
        List<Store> listStore;
        if (!iCityRepository.existsByCountrySlug(storeRequest.getCountry())){
            // List<String> messages = new ArrayList<>();
            // messages.add(localizationUtil.getLocalizedMessage(MessageKey.COUNTRY_NOT_FOUND));
            // return new APIResponse<>(null, messages);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.COUNTRY_NOT_FOUND));
        }
        if (storeRequest.getCity() != null && !storeRequest.getCity().isEmpty()) {
            if (!iCityRepository.existsByName(storeRequest.getCity())) {
                // List<String> messages = new ArrayList<>();
                // messages.add(localizationUtil.getLocalizedMessage(MessageKey.CITY_NOT_FOUND));
                // return new APIResponse<>(null, messages);
                throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.CITY_NOT_FOUND));
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

    // @Override
    // public APIResponse<Boolean> uploadMedia(String storeID, MultipartFile[] images, String type) {
    //     try {
    //         if ("THUMBNAIL".equalsIgnoreCase(type)) {
    //             return handleUploadThumbnail(storeID, images[0]);
    //         } else if ("GALLERY".equalsIgnoreCase(type)) {
    //             return handleUploadGallery(storeID, images);
    //         } else {
    //             List<String> messages = new ArrayList<>();
    //             messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_NOT_VALID_FILES_UPLOADED));
    //             return new APIResponse<>(false, messages);
                
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         List<String> messages = new ArrayList<>();
    //         messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_UPLOAD_MEDIA_FAILED));
    //         return new APIResponse<>(false, messages);
    //     }
    // }

    // private APIResponse<Boolean> handleUploadThumbnail(String storeID, MultipartFile file) throws NotFoundException {
    //     Store store = iStoreRepository.findById(UUID.fromString(storeID)).orElse(null);
    //     if (store == null) {
    //         // List<String> messages = new ArrayList<>();
    //         // messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_NOT_FOUND));
    //         // return new APIResponse<>(false, messages);
    //         throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.STORE_NOT_FOUND));
    //     }

    //     try {
    //         if (store.getThumbnailMedia() != null) {
    //             // Xóa media cũ nếu có
    //             cloudinaryService.deleteMedia(store.getThumbnailMedia().getPublicId());
    //             store.setThumbnailMedia(new Media());
                
    //         }
    //         Map uploadResult = cloudinaryService.uploadMedia(file, "stores");
    //         String imageURL = (String) uploadResult.get("secure_url");

    //         Media thumbnailMedia = Media.builder()
    //                 .imageUrl(imageURL)
    //                 .publicId((String) uploadResult.get("public_id"))
    //                 .referenceId(store.getId())
    //                 .referenceType("STORE")
    //                 .altText("Thumbnail for store: " + store.getStoreName())
    //                 .type("THUMBNAIL")
    //                 .build();

    //         store.setThumbnailMedia(thumbnailMedia);
    //         iStoreRepository.save(store);

    //         List<String> messages = new ArrayList<>();
    //         messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_UPLOAD_THUMBNAIL_SUCCESS));
    //         return new APIResponse<>(true, messages);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         List<String> messages = new ArrayList<>();
    //         messages.add(localizationUtil.getLocalizedMessage(MessageKey.STORE_UPLOAD_THUMBNAIL_FAILED));
    //         return new APIResponse<>(false, messages);
    //     }
    // }

    @Override
    public APIResponse<Boolean> handleUploadGallery(String storeID, MultipartFile[] images) throws NotFoundException {
        Store store = iStoreRepository.findById(UUID.fromString(storeID)).orElse(null);
        if (store == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.STORE_NOT_FOUND));
        }

        try {
            store.getImage().stream()
                .filter(media -> "GALLERY".equals(media.getType()) && media.getPublicId() != null)
                .forEach(media -> {
                    cloudinaryService.deleteMedia(media.getPublicId());
                });
            store.getImage().clear();
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
            store.getImage().addAll(newImages);
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
