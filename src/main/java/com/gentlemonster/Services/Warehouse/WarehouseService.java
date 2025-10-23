package com.gentlemonster.Services.Warehouse;

import com.gentlemonster.Contants.MessageKey;
import com.gentlemonster.DTO.Requests.Warehouse.AddWarehouseRequest;
import com.gentlemonster.DTO.Requests.Warehouse.EditWarehouseRequest;
import com.gentlemonster.DTO.Requests.Warehouse.WarehouseRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.Warehouse.BaseWarehouseResponse;
import com.gentlemonster.DTO.Responses.Warehouse.WarehouseResponse;
import com.gentlemonster.Entities.Media;
import com.gentlemonster.Entities.User;
import com.gentlemonster.Entities.Warehouse;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Repositories.IUserRepository;
import com.gentlemonster.Repositories.IWarehouseRepository;
import com.gentlemonster.Services.Cloudinary.CloudinaryService;
import com.gentlemonster.Utils.LocalizationUtils;
import com.gentlemonster.Utils.ValidationUtils;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class WarehouseService implements  IWarehouseService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ValidationUtils vietnameseStringUtils;
    @Autowired
    private LocalizationUtils localizationUtil;
    @Autowired
    private IWarehouseRepository iWarehouseRepository;
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public PagingResponse<List<BaseWarehouseResponse>> getAllWarehouse(WarehouseRequest warehouseRequest) {
        List<BaseWarehouseResponse> baseWarehouseResponses;
        List<Warehouse> warehousesList;
        Pageable pageable;
        if (warehouseRequest.getPage() == 0 && warehouseRequest.getLimit() == 0){
            warehousesList = iWarehouseRepository.findAll();
            baseWarehouseResponses = warehousesList.stream()
                    .map(warehouse -> modelMapper.map(warehouse, BaseWarehouseResponse.class))
                    .toList();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_GET_SUCCESS));
            return new PagingResponse<>(baseWarehouseResponses, messages, 1, (long) baseWarehouseResponses.size());
        }else {
            warehouseRequest.setPage((Math.max(warehouseRequest.getPage(), 1)));
            pageable = PageRequest.of(warehouseRequest.getPage() - 1, warehouseRequest.getLimit());
        }
        Page<Warehouse> warehousePage = iWarehouseRepository.findAll(pageable);
        warehousesList = warehousePage.getContent();
        baseWarehouseResponses = warehousesList.stream()
                .map(warehouse -> modelMapper.map(warehouse, BaseWarehouseResponse.class))
                .toList();

        if (baseWarehouseResponses.isEmpty()){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_EMPTY));
            return new PagingResponse<>(baseWarehouseResponses, messages, 1, (long) baseWarehouseResponses.size());
        }
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_GET_SUCCESS));
        return new PagingResponse<>(baseWarehouseResponses, messages, warehouseRequest.getPage(), warehousePage.getTotalElements());
    }

    @Override
    public APIResponse<Boolean> addWarehouse(AddWarehouseRequest addWarehouseRequest) {
        User user = iUserRepository.findByFullNameAndCode(addWarehouseRequest.getFullName(), addWarehouseRequest.getCode()).orElse(null);
        if (iWarehouseRepository.existsByWarehouseName(addWarehouseRequest.getWarehouseName())){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_EXISTED));
            return new APIResponse<>(false, messages);
        }
        if (user == null){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.FULLNAME_MISMATCH));
            return new APIResponse<>(false, messages);
        }
        Warehouse warehouse = modelMapper.map(addWarehouseRequest, Warehouse.class);
        warehouse.setWarehouseLocation(addWarehouseRequest.getWarehouseLocation());
        String prefixCodeUser = addWarehouseRequest.getCode().substring(0, addWarehouseRequest.getCode().indexOf("-"));
        if (prefixCodeUser.equals("WH")){
            warehouse.getUsers().add(user);
        }else{
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_WRONG_ROLE)));
        }
        iWarehouseRepository.save(warehouse);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_CREATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> updateWarehouse(String warehouseID , EditWarehouseRequest editWarehouseRequest) throws NotFoundException {
        Warehouse warehouse = iWarehouseRepository.findById(UUID.fromString(warehouseID)).orElse(null);
        User user = iUserRepository.findByFullNameAndCode(editWarehouseRequest.getFullName(), editWarehouseRequest.getCode()).orElse(null);
        if (warehouse == null){
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
        }
        if (user == null){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.FULLNAME_MISMATCH));
            return new APIResponse<>(false, messages);
        }
        modelMapper.map(editWarehouseRequest, warehouse);
        warehouse.getUsers().add(user);
        iWarehouseRepository.save(warehouse);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_UPDATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> deleteWarehouse(String warehouseId) throws NotFoundException {
        Warehouse warehouse = iWarehouseRepository.findById(UUID.fromString(warehouseId)).orElse(null);
        if (warehouse == null){
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
        }
        iWarehouseRepository.delete(warehouse);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<WarehouseResponse> getWarehouseById(String warehouseId) throws NotFoundException {
        Warehouse warehouse = iWarehouseRepository.findById(UUID.fromString(warehouseId)).orElse(null);
        if (warehouse == null){
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
        }
        WarehouseResponse warehouseResponse = modelMapper.map(warehouse, WarehouseResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_GET_SUCCESS));
        return new APIResponse<>(warehouseResponse, messages);
    }

    @Override
    public APIResponse<Boolean> handleUploadImage(String warehouseID, MultipartFile[] images) throws NotFoundException {
        Warehouse warehouse = iWarehouseRepository.findById(UUID.fromString(warehouseID)).orElse(null);
        if (warehouse == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
        }
        try {
           warehouse.getImage().stream()
                   .filter(media -> "GALLERY".equals(media.getType()) && media.getPublicId() != null)
                   .forEach(media -> {
                       cloudinaryService.deleteMedia(media.getPublicId());
                   });
            warehouse.getImage().clear();
            List<Media> newImages = Arrays.stream(images)
                    .filter(image -> image != null && !image.isEmpty())
                    .map(image -> {
                        String originalFilename = image.getOriginalFilename();
                        if (originalFilename == null || !originalFilename.contains(".")) {
                            throw new IllegalArgumentException("Invalid image file name: " + originalFilename);
                        }
                        Map<String, Object> uploadResult = cloudinaryService.uploadMedia(image, "stories");
                        String imageURL = (String) uploadResult.get("secure_url");
                        return Media.builder()
                            .imageUrl(imageURL)
                            .publicId((String) uploadResult.get("public_id"))
                            .altText("Gallery image for warehouse")
                            .referenceId(warehouse.getId()) // Replace with appropriate logic
                            .referenceType("WAREHOUSE")
                            .type("GALLERY")
                            .build();
                    }).collect(Collectors.toList());
                warehouse.getImage().addAll(newImages);
                iWarehouseRepository.save(warehouse);
                List<String> messages = new ArrayList<>();
                messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_UPLOAD_MEDIA_SUCCESS));
                return new APIResponse<>(true, messages);
        } catch (Exception e) {
            e.printStackTrace();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_UPLOAD_MEDIA_FAILED));
            return new APIResponse<>(false, messages);
        }
    }

    @Override
    public APIResponse<WarehouseResponse> deleteUserWarehouse(String warehouseId, String userId)
            throws NotFoundException {
        Warehouse warehouse = iWarehouseRepository.findById(UUID.fromString(warehouseId)).orElse(null);
        if (warehouse == null){
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
        }
        User user = iUserRepository.findById(UUID.fromString(userId)).orElse(null);
        if (user == null){
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND));
        }
        if (warehouse.getUsers() == null || !warehouse.getUsers().stream().anyMatch(u -> u.getId().equals(user.getId()))){
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_USER_MISMATCH));
        }
        warehouse.getUsers().remove(user);
        iWarehouseRepository.save(warehouse);
        WarehouseResponse warehouseResponse = modelMapper.map(warehouse, WarehouseResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_UPDATE_SUCCESS));
        return new APIResponse<>(warehouseResponse, messages);
    }

    @Override
    public APIResponse<WarehouseResponse> addUserWarehouse(String warehouseId, String userId) throws NotFoundException {
        Warehouse warehouse = iWarehouseRepository.findById(UUID.fromString(warehouseId)).orElse(null);
        if (warehouse == null){
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_NOT_FOUND));
        }
        User user = iUserRepository.findById(UUID.fromString(userId)).orElse(null);
        if (user == null){
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND));
        }
        String prefixCodeUser = user.getCode().substring(0, user.getCode().indexOf("-"));
        if (!prefixCodeUser.equals("WH")){
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_WRONG_ROLE)));
        }
        if (warehouse.getUsers() != null && warehouse.getUsers().stream().anyMatch(u -> u.getId().equals(user.getId()))){
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_USER_EXISTED)));
        }
        warehouse.getUsers().add(user);
        iWarehouseRepository.save(warehouse);
        WarehouseResponse warehouseResponse = modelMapper.map(warehouse, WarehouseResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.WAREHOUSE_UPDATE_SUCCESS));
        return new APIResponse<>(warehouseResponse, messages);
    }
}
