package com.gentlemonster.GentleMonsterBE.Services.User;

import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.User.AddUserResquest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.User.AddressCustomerRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.User.EditUserRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.User.UserRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Address.AddressCustomerResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.User.BaseUserResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.User.UserInforResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.User.UserResponse;
import com.gentlemonster.GentleMonsterBE.Entities.Address;
import com.gentlemonster.GentleMonsterBE.Entities.AuthToken;
import com.gentlemonster.GentleMonsterBE.Entities.Media;
import com.gentlemonster.GentleMonsterBE.Entities.Role;
import com.gentlemonster.GentleMonsterBE.Entities.User;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;
import com.gentlemonster.GentleMonsterBE.Repositories.IAddressRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IRoleRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IStoreRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.ITokenRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IUserRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.Specification.UserSpecification;
import com.gentlemonster.GentleMonsterBE.Services.Cloudinary.CloudinaryService;
import com.gentlemonster.GentleMonsterBE.Utils.JwtTokenUtils;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtils;
import com.gentlemonster.GentleMonsterBE.Utils.ValidationUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private IRoleRepository iRoleRepository;
    @Autowired
    private IStoreRepository iStoreRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LocalizationUtils localizationUtil;
    @Autowired
    private PasswordEncoder passwordEncode;
    @Autowired
    private IAddressRepository iAddressRepository;
    @Autowired
    private ValidationUtils vietnameseStringUtils;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ITokenRepository iTokenRepository;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    public PagingResponse<List<BaseUserResponse>> getAllUser(@ModelAttribute UserRequest userRequest) {
        List<BaseUserResponse> userResponseList;
        List<User> userList;
        Pageable pageable;
        if(userRequest.getPage() == 0 && userRequest.getLimit() == 0){
            userList = iUserRepository.findAll();
            userResponseList = userList.stream()
                    .map(user -> modelMapper.map(user,BaseUserResponse.class))
                    .toList();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_GET_SUCCESS));
            return new PagingResponse<>(userResponseList,messages,1,(long) userResponseList.size());
        }else{
            userRequest.setPage(Math.max(userRequest.getPage(),1));
            pageable = PageRequest.of(userRequest.getPage() - 1, userRequest.getLimit());
        }
//        Specification<User> specification = UserSpecification.filterUsers(userRequest.getEmployeeCode(),userRequest.getFullName(),userRequest.getEmail());
//        Page<User> userPage = iUserRepository.findAll(specification,pageable);
        Page<User> userPage = iUserRepository.findAll(pageable);
        userList = userPage.getContent();
        userResponseList = userList.stream()
                .map(user -> modelMapper.map(user,BaseUserResponse.class))
                .toList();
        if(userResponseList.isEmpty()){
            return new PagingResponse<>(null,List.of(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_EXIST)),1,0L);
        }
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_GET_SUCCESS));
        return new PagingResponse<>(userResponseList,messages,userPage.getTotalPages(),userPage.getTotalElements());
    }

    @Override
    public PagingResponse<List<UserResponse>> getAllUserByRole(UserRequest userRequest) {
        List<UserResponse> userResponseList;
        List<User> userList;
        Pageable pageable;

        int page = Math.max(userRequest.getPage() - 1, 0); // Page index should start from 0
        int size = userRequest.getLimit() > 0 ? userRequest.getLimit() : 10; // Default size is 10 if limit is not provided
        pageable = PageRequest.of(page, size, Sort.by("fullName").descending());

        // Specification<User> specification = UserSpecification.filterUsers(userRequest.getRole(), userRequest.getName(), userRequest.getEmail());
        Page<User> userPage = iUserRepository.findAll(pageable);
        userList = userPage.getContent();

        // Map the User entities to UserResponse DTOs
        userResponseList = userList.stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();

        if(userResponseList.isEmpty()){
            return new PagingResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_EXIST)), 1, 0L);
        }
        // Prepare the response
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_GET_SUCCESS));
        return new PagingResponse<>(userResponseList, messages, userPage.getTotalPages(), userPage.getTotalElements());
    }

    @Override
    @Transactional
    public APIResponse<Boolean> addUser(AddUserResquest addUserRequest) throws NotFoundException {
        if(iUserRepository.existsByEmail(addUserRequest.getEmail())){
            return new APIResponse<>(null,List.of(localizationUtil.getLocalizedMessage(MessageKey.USER_ALREADY_EXIST)));
        }
        if(iUserRepository.existsByPhoneNumber(addUserRequest.getPhoneNumber())){
            return new APIResponse<>(null,List.of(localizationUtil.getLocalizedMessage(MessageKey.USER_PHONE_EXISTED)));
        }
        if(addUserRequest.getEmail().isEmpty() || !addUserRequest.getEmail().contains("@")){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_EMAIL_INVALID));
            return new APIResponse<>(null,messages);
        }
        if(addUserRequest.getRole().isEmpty()){
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_ROLE_NULL));
            return new APIResponse<>(null,messages);
        }
        if (!iRoleRepository.existsByName(addUserRequest.getRole())){
            return new APIResponse<>(null,List.of(localizationUtil.getLocalizedMessage(MessageKey.ROLE_NOT_EXIST)));
        }

        User user = modelMapper.map(addUserRequest,User.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(addUserRequest.getYear(), addUserRequest.getMonth() - 1, addUserRequest.getDay());
        Date birthday = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(birthday);
        try {
            user.setBirthDay(dateFormat.parse(formattedDate));
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format: " + formattedDate, e);
        }

        user.setPassword(passwordEncode.encode(addUserRequest.getPassword()));
        user.setFullName(addUserRequest.getFirstName() + " " + addUserRequest.getMiddleName() + " " + addUserRequest.getLastName());
        String slugStandardization = vietnameseStringUtils.removeAccents(addUserRequest.getFirstName() + " " + addUserRequest.getMiddleName() + " " + addUserRequest.getLastName()).toLowerCase();
        user.setSlug(slugStandardization);
        // Xử lý danh sách địa chỉ
        Address address = Address.builder()
                .name(addUserRequest.getFirstName() + " " + addUserRequest.getMiddleName() + " " + addUserRequest.getLastName())
                .phoneNumber(addUserRequest.getPhoneNumber())
                .street(addUserRequest.getStreet())
                .ward(addUserRequest.getWard())
                .district(addUserRequest.getDistrict())
                .city(addUserRequest.getCity())
                .country(addUserRequest.getCountry())
                .isDefault(false) // true: địa chỉ này sẽ là địa chỉ mặc định, false: địa chỉ này sẽ không phải là địa chỉ mặc định
                .type(false) // true: địa chỉ này là địa chỉ nhà, false: địa chỉ này là địa chỉ văn phòng
                .user(user)
                .build();

        // Kiểm tra xem người dùng có địa chỉ hay không
        if(address == null) {
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage("Address is required")));
        }
        // Thêm địa chỉ vào danh sách của người dùng
        user.getAddresses().add(address);

        // Thiết lập role cho người dùng
        Role role = iRoleRepository.findByName(addUserRequest.getRole()).orElse(null);
        user.setRole(role);
        if (role == null) {
            // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage("Role not found")));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.ROLE_NOT_FOUND));
        }
        // Tạo mã code dựa trên role
        user.setCode(generateCode(role));
        // if(addUserRequest.getRole().equals("STORE_MANAGER") || addUserRequest.getRole().equals("EMPLOYEE")){
        // Store userStore = iStoreRepository.findByStoreName(addUserRequest.getStore()).orElse(null);
        // if (userStore == null) {
        //     return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.STORE_WRONG_ROLE)));
        // }
        // user.setStore(userStore);
        // }else {
        //     iUserRepository.save(user);
        // }
        iUserRepository.save(user);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_CREATE_SUCCESS));
        return new APIResponse<>(true,messages);
    }

    @Override
    public APIResponse<UserResponse> getOneUser(String userID) throws NotFoundException {
        User user = iUserRepository.findById(UUID.fromString(userID)).orElse(null);
        if(user == null){
            // return new APIResponse<>(null,List.of(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND));
        }
        UserResponse userResponse = modelMapper.map(user,UserResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_GET_ONE_SUCCESS));
        return new APIResponse<>(userResponse,messages);
    }

    @Override
    public APIResponse<Boolean> updateUser(String userID, EditUserRequest editUserRequest) throws NotFoundException {
        User user = iUserRepository.findById(UUID.fromString(userID)).orElse(null);
        Role role = iRoleRepository.findByName(editUserRequest.getRole()).orElse(null);
        if (user == null){
            // return new APIResponse<>(null,List.of(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND));
        }
        if (role == null){
            // return new APIResponse<>(null,List.of(localizationUtil.getLocalizedMessage(MessageKey.ROLE_NOT_)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.ROLE_NOT_FOUND));
        }

        modelMapper.map(editUserRequest,user);
        user.setRole(role);
        Calendar calendar = Calendar.getInstance();
        calendar.set(editUserRequest.getYear(), editUserRequest.getMonth() - 1, editUserRequest.getDay());
        //Kiểm tra ngày hợp lệ
        Date birthday = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = dateFormat.format(birthday);
        try {
            user.setBirthDay(dateFormat.parse(formattedDate));
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format: " + formattedDate, e);
        }
        user.setPassword(passwordEncode.encode(editUserRequest.getPassword()));
        user.setFullName(editUserRequest.getFirstName() + " " + editUserRequest.getMiddleName() + " " + editUserRequest.getLastName());
        String slugStandardization = vietnameseStringUtils.removeAccents(editUserRequest.getTitle() + "+" + editUserRequest.getFirstName() + "+" + editUserRequest.getLastName()).toLowerCase().trim();
        user.setSlug(slugStandardization);
        // Xóa địa chỉ cũ khỏi cơ sở dữ liệu
        if (!user.getAddresses().isEmpty()) {
            iAddressRepository.deleteAll(user.getAddresses()); // Phải có iAddressRepository
            user.getAddresses().clear();
        }
        Address address = Address.builder()
                .street(editUserRequest.getStreet())
                .ward(editUserRequest.getWard())
                .district(editUserRequest.getDistrict())
                .city(editUserRequest.getCity())
                .country(editUserRequest.getCountry())
                .user(user)
                .build();
        user.getAddresses().add(address);
        user.setModifiedDate(LocalDateTime.now());
        // if(editUserRequest.getRole().equals("STORE_MANAGER") || editUserRequest.getRole().equals("EMPLOYEE")){
        //     Store userStore = iStoreRepository.findByStoreName(editUserRequest.getStore()).orElse(null);
        //     if (userStore == null) {
        //         return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.STORE_WRONG_ROLE)));
        //     }
        //     user.setStore(userStore);
        // }else {
        //     iUserRepository.save(user);
        // }
        iUserRepository.save(user);
        // UserResponse userResponse = modelMapper.map(user,UserResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_UPDATE_SUCCESS));
        return new APIResponse<>(true,messages);
    }

    @Override
    public APIResponse<Boolean> deleteUser(String userID) throws NotFoundException {
        User user = iUserRepository.findById(UUID.fromString(userID)).orElse(null);
        if (user == null) {
            // return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND)));
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND));
        }
        Media avatar = user.getImage();
        if (avatar != null && avatar.getPublicId() != null) {
            // Xóa media thumbnail nếu có
            cloudinaryService.deleteMedia(avatar.getPublicId());
        }
        AuthToken authToken = iTokenRepository.findByUserId(user.getId()).orElse(null);
        if (authToken != null) {
            // // Xóa token liên quan đến người dùng
            iTokenRepository.delete(authToken);
        }
        
        iUserRepository.delete(user);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_DELETE_SUCCESS));
        return new APIResponse<>(true,messages);
    }

    @Override
    public APIResponse<List<BaseUserResponse>> searchUser(@ModelAttribute UserRequest userRequest) {
        List<BaseUserResponse> userResponseList;
        List<User> userList;
        String slugStandardization = null;
        // Kiểm tra nếu cả email và fullName đều null
        if (userRequest.getName() == null && userRequest.getEmail() == null) {
            return new APIResponse<>(null,List.of("Email and Full Name are null !"));
        }

        // Kiểm tra email nếu được cung cấp
        if (userRequest.getEmail() != null && !userRequest.getEmail().isEmpty()) {
            boolean emailExists = iUserRepository.existsByEmail(userRequest.getEmail());
            if (!emailExists) {
                return new APIResponse<>(null, List.of("Email not exist !"));
            }
        }else{
            return new APIResponse<>(null, List.of("Email is null !"));
        }

        if (userRequest.getName() != null && !userRequest.getName().isEmpty()) {
            slugStandardization = userRequest.getName().trim()
                    .replace(" ", "+")
                    .toLowerCase();
            slugStandardization = vietnameseStringUtils.removeAccents(slugStandardization);
            boolean fullNameExists = iUserRepository.existsBySlug(slugStandardization);
            if (!fullNameExists) {
                return new APIResponse<>(null, List.of("Full Name not exist !"));
            }
        }else{
            return new APIResponse<>(null, List.of("Full Name is null !"));
        }

        Specification<User> specification = UserSpecification.filterUsers(userRequest.getEmail(), slugStandardization, userRequest.getCode());
        userList = iUserRepository.findAll(specification);
        if(userList.isEmpty()){
            return new APIResponse<>(null, List.of("List user search is null"));
        }
        userResponseList = userList.stream()
                .map(user -> modelMapper.map(user, BaseUserResponse.class))
                .toList();

        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_GET_SUCCESS));
        return new APIResponse<>(userResponseList, messages);
    }

    @Override
    public APIResponse<Boolean> deleteMutiUser(List<String> userIDs) {
        return null;
    }

    // Hàm tạo mã code của người dùng dựa trên tên của role và mã này chỉ tối đa 11 kí tự
    public String generateCode(Role role) {
        String prefix;
        switch (role.getName().toUpperCase()) {
            case "ADMIN":
                prefix = "AD";
                break;
            case "CUSTOMER":
                prefix = "CS";
                break;
            case "EMPLOYEE":
                prefix = "EM";
                break;
            case "STORAGE_MANAGER":
                prefix = "SRM";
                break;
            case "STORE_MANAGER":
                prefix = "SM";
                break;
            default:
                prefix = "NO"; // Mặc định nếu role không xác định
                break;
        }
        String code;
        do {
            // Tạo số ngẫu nhiên từ 0 đến 100 và đảm bảo có tối đa 8 chữ số
            int randomNumber = new Random().nextInt(101);
            code = prefix+ "-" + String.format("%08d", randomNumber); // Đảm bảo có 8 chữ số sau prefix
        } while (iUserRepository.existsByCode(code)); // Kiểm tra xem mã đã tồn tại chưa

        return code;
    }

    @Override
    public APIResponse<Boolean> uploadAvatarEmployee(String userID, MultipartFile image) throws NotFoundException {
        User user = iUserRepository.findById(UUID.fromString(userID)).orElse(null);
        if (user == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND));
        }
        try {
            
            Map uploadResult = cloudinaryService.uploadMedia(image, "users");
            String imageUrl = (String) uploadResult.get("secure_url");
            Media media = Media.builder()
                    .imageUrl(imageUrl)
                    .publicId((String) uploadResult.get("public_id"))
                    .referenceId(user.getId())
                    .referenceType("EMPLOYEE")
                    .altText("Avatar: " + user.getFullName())
                    .type("IMAGE")
                    .build();
            user.setImage(media);
            iUserRepository.save(user);
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_UPLOAD_AVATAR_SUCCESS));
            return new APIResponse<>(true, messages);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.USER_UPLOAD_AVATAR_FAILED)));
        }

    }
    
}
