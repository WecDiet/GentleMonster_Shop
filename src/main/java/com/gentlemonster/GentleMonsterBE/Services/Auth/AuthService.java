package com.gentlemonster.GentleMonsterBE.Services.Auth;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.ChangePasswordRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.ChangeUserInfoRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.UserRegisterRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.User.AddressCustomerRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Address.AddressCustomerResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Auth.UserLoginResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.User.UserInforResponse;
import com.gentlemonster.GentleMonsterBE.Entities.Address;
import com.gentlemonster.GentleMonsterBE.Entities.AuthToken;
import com.gentlemonster.GentleMonsterBE.Entities.Media;
import com.gentlemonster.GentleMonsterBE.Entities.Role;
import com.gentlemonster.GentleMonsterBE.Entities.User;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;
import com.gentlemonster.GentleMonsterBE.Repositories.IAddressRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IAuthRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IRoleRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.ITokenRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IUserRepository;
import com.gentlemonster.GentleMonsterBE.Services.Cloudinary.CloudinaryService;
import com.gentlemonster.GentleMonsterBE.Services.Token.TokenService;
import com.gentlemonster.GentleMonsterBE.Utils.JwtTokenUtils;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtils;
import com.gentlemonster.GentleMonsterBE.Utils.ValidationUtils;

import ch.qos.logback.core.model.Model;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@NoArgsConstructor
@Slf4j
public class AuthService implements IAuthService{

    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private IAuthRepository authRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LocalizationUtils localizationUtil;
    @Autowired
    private ValidationUtils vietnameseStringUtils;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ITokenRepository iTokenRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private IAddressRepository iAddressRepository;

    @Override
    @Transactional
    public APIResponse<Boolean> createUser(UserRegisterRequest userRegisterRequest) throws Exception {
        List<String> errorMessages = new ArrayList<>();

        // Kiểm tra email đã tồn tại chưa
        if (authRepository.findByEmail(userRegisterRequest.getEmail()).isPresent()) {
            errorMessages.add("Email is already exist!");
        }

        // Kiểm tra email xác nhận có trùng khớp không
        if (!userRegisterRequest.getEmail().equals(userRegisterRequest.getConfirmEmail())) {
            errorMessages.add("Email and Confirm Email do not match!");
        }

        // Kiểm tra mật khẩu xác nhận có trùng khớp không
        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getConfirmPassword())) {
            errorMessages.add("Password and Confirm Password do not match!");
        }

        // Nếu có lỗi thì trả về ngay, không tiếp tục xử lý
        if (!errorMessages.isEmpty()) {
            return new APIResponse<>(false, errorMessages);
        }
            Calendar calendar = Calendar.getInstance();
            calendar.set(userRegisterRequest.getYearBirthDay(), userRegisterRequest.getMonthBirthDay() - 1, userRegisterRequest.getDayBirthDay(), 0, 0, 0);
            Date birthday = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(birthday);

            // Tìm Role CUSTOMER
            Role role = roleRepository.findByName("CUSTOMER")
                    .orElseThrow(() -> new NotFoundException("Role 'CUSTOMER' not found in database!"));

            // Mã hóa mật khẩu trước khi lưu
            String encodedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());

            String slugStandardization = vietnameseStringUtils.removeAccents(userRegisterRequest.getFirstName() + " " + userRegisterRequest.getMiddleName() + " " + userRegisterRequest.getLastName()).toLowerCase();
            // Tạo user mới
            User newUser = User.builder()
                    .email(userRegisterRequest.getEmail())
                    .password(encodedPassword) // Mật khẩu đã mã hóa
                    .title(userRegisterRequest.getTitle())
                    .firstName(userRegisterRequest.getFirstName())
                    .middleName(userRegisterRequest.getMiddleName() != null ? userRegisterRequest.getMiddleName() : "")
                    .lastName(userRegisterRequest.getLastName())
                    .fullName(
                            (userRegisterRequest.getFirstName() + " " +
                                    (userRegisterRequest.getMiddleName() != null ? userRegisterRequest.getMiddleName() + " " : "") +
                                    userRegisterRequest.getLastName()).trim()
                    )
                    .slug(slugStandardization)
                    // .username(slugStandardization + "-" + new Random().nextInt(1000)) // Tạo username duy nhất
                    .username(userRegisterRequest.getFirstName() + userRegisterRequest.getMiddleName() + userRegisterRequest.getLastName() + new Random().nextInt(1000)) // Tạo username duy nhất
                    .birthDay(dateFormat.parse(formattedDate))
                    .role(role)
                    .code(generateCode(role))
                    .userType(3) // 1: Google, 2: Facebook, 3: Email
                    .status(true) // true: Active, false: Deactive
                    .build();

            // Lưu user vào DB
            authRepository.save(newUser);

            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.REGISTER_SUCCESSFULLY));
            // Trả về phản hồi thành công
            return new APIResponse<>(true, messages);
    }

    // Hàm tạo mã ngẫu nhiên cho user
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
    public APIResponse<UserLoginResponse> login(String login, String password, String tokenType, String deviceToken, String deviceName) throws Exception {
        Optional<User> userOptional;
        if(login.contains("@")){
            userOptional = authRepository.findByEmail(login);
        }else{
            userOptional = authRepository.findByUsername(login);
        }
        if (userOptional.isEmpty()) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_EXIST));
        }
        User existingUser = userOptional.get();
        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException(localizationUtil.getLocalizedMessage(MessageKey.WRONG_INPUT));
        }else if (!existingUser.isStatus()) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.ACCOUNT_LOCKED));
        }
        
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        login, password,
                        existingUser.getAuthorities()
                    );
        authenticationManager.authenticate(authenticationToken);

        Optional<AuthToken> existingToken = tokenService.findByToken(existingUser, tokenType, deviceToken, deviceName);
        String accessToken;
        String refreshToken;
        AuthToken authToken;
        if (existingToken.isPresent() && jwtTokenUtils.isValidToken(existingToken.get().getToken()) && jwtTokenUtils.isValidToken(existingToken.get().getRefreshToken())){
            authToken = existingToken.get();
            accessToken = authToken.getToken();
            refreshToken = authToken.getRefreshToken();

        }else{
            if (existingToken.isPresent()) {
                // Nếu token đã tồn tại nhưng không hợp lệ, xóa token cũ
                iTokenRepository.delete(existingToken.get());
            }
            accessToken = jwtTokenUtils.generateToken(existingUser);
            refreshToken = jwtTokenUtils.generateRefreshToken(existingUser);
            authToken = tokenService.saveToken(accessToken, existingUser, refreshToken, tokenType, deviceToken, deviceName);
        }

        UserLoginResponse response = UserLoginResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .tokenType(authToken.getTokenType())
                .deviceToken(authToken.getDeviceToken())
                .deviceName(authToken.getDeviceName())
                .build();
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.LOGIN_SUCCESSFULLY));
        return new APIResponse<>(response, messages);
    }

    @Override
    @Transactional
    public String changeUserPassword(ChangePasswordRequest changePassword, String token) throws NotFoundException {
        String jwtToken = token.substring(7).trim(); // Loại bỏ "Bearer " khỏi token
        // Lấy subject từ token (email cho CUSTOMER, username cho EMPLOYEE)
        String subject = jwtTokenUtils.extractSubject(jwtToken);
        Optional<User> userOptional;
        if (jwtTokenUtils.extractClaim(jwtToken, claims -> claims.get("role", String.class)).equalsIgnoreCase("CUSTOMER")) {
            userOptional = authRepository.findByEmail(subject);
        } else {
            userOptional = authRepository.findByUsername(subject);
        }
        if (userOptional.isEmpty()) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_EXIST));
        }

        User user = userOptional.get();
        if (changePassword.getNewPassword().equals(changePassword.getCurrentPassword())) {
            throw new BadCredentialsException(localizationUtil.getLocalizedMessage(MessageKey.DIFFERENT_PASSWORD));
        }
        if (!passwordEncoder.matches(changePassword.getCurrentPassword(), changePassword.getNewPassword())) {
            throw new BadCredentialsException(localizationUtil.getLocalizedMessage(MessageKey.WRONG_INPUT));
        }
        if (!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
            throw new BadCredentialsException(localizationUtil.getLocalizedMessage(MessageKey.PASSWORD_CONFIRM_NOT_MATCH));
        }

        String encoderNewPassword = passwordEncoder.encode(changePassword.getNewPassword());
        user.setPassword(encoderNewPassword);
        user.setModifiedDate(LocalDateTime.now());
        authRepository.save(user);

        // Tùy chọn: Thu hồi tất cả token hiện tại của user để buộc đăng nhập lại
        List<AuthToken> userTokens = iTokenRepository.findByUser(user);
        userTokens.forEach(authToken -> authToken.setRevoked(true));
    
        iTokenRepository.saveAll(userTokens);
        return localizationUtil.getLocalizedMessage(MessageKey.UPDATE_PASSWORD_SUCCESSFULLY);
    }

    @Override
    public UserLoginResponse changeUserInfo(ChangeUserInfoRequest changeUserInfo) {
        return null;
    }

    @Override
    public User registerGoogle(String token) {
        return null;
    }

    @Override
    public String loginGoogle(String token, String deviceToken) throws Exception {
        return "";
    }

    @Override
    public User registerFacebook(String token) {
        return null;
    }

    @Override
    public String loginFacebook(String token, String deviceToken) throws Exception {
        return "";
    }

    @Override
    public APIResponse<List<AddressCustomerResponse>> getAllAddressByCustomer(String token) throws NotFoundException
    {
        String jwt = token.substring(7).trim();
        if (jwtTokenUtils.isTokenExpired(jwt)) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.TOKEN_EXPIRED));
        }
        String subject = jwtTokenUtils.extractSubject(jwt);
        Optional<User> userOptional;
        if (jwtTokenUtils.extractClaim(jwt, claims -> claims.get("email", String.class)) == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND));
        }
        userOptional = iUserRepository.findByEmail(subject);
        User user = userOptional.get();
        List<AddressCustomerResponse> addressCustomerResponses = user.getAddresses().stream()
                .map(address -> modelMapper.map(address, AddressCustomerResponse.class))
                .toList();
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_ADDRESS_GET_SUCCESS));
        return new APIResponse<>(addressCustomerResponses, messages);
        
    }

    @Override
    public APIResponse<Boolean> uploadAvatarCustomer(String token, MultipartFile image) throws NotFoundException {
        String jwt = token.substring(7).trim();
        if (jwtTokenUtils.isTokenExpired(jwt)) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.TOKEN_EXPIRED));
        }
        String subject = jwtTokenUtils.extractSubject(jwt);
        Optional<User> userOptional;
        if (jwtTokenUtils.extractClaim(jwt, claims -> claims.get("email", String.class)) == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND));
        }
        userOptional = iUserRepository.findByEmail(subject);
        User user = userOptional.get();
        Map uploadResult = cloudinaryService.uploadMedia(image, "customers");
        String imageUrl = (String) uploadResult.get("secure_url");
        Media media = Media.builder()
                .imageUrl(imageUrl)
                .publicId((String) uploadResult.get("public_id"))
                .referenceId(user.getId())
                .referenceType("CUSTOMER")
                .altText("Avatar: " + user.getFullName())
                .type("IMAGE")
                .build();
        user.setImage(media);
        iUserRepository.save(user);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_UPLOAD_AVATAR_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> verifyPassword(String token, String password) throws NotFoundException {
        String jwtToken = token.substring(7).trim();
        if (jwtTokenUtils.isTokenExpired(jwtToken)) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.TOKEN_EXPIRED));
        }
        String subject = jwtTokenUtils.extractSubject(jwtToken);
        if (jwtTokenUtils.extractClaim(jwtToken, claims -> claims.get("email", String.class)) == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND));
        }
        Optional<User> userOptional = iUserRepository.findByEmail(subject);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_EXIST));
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException(localizationUtil.getLocalizedMessage(MessageKey.WRONG_INPUT));
        }
        ArrayList<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_GET_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public UserInforResponse getUserLoginResponse(String login) throws NotFoundException {
        if (login == null || !login.startsWith("Bearer ")) {
            log.error("Invalid or missing Authorization header");
            throw new IllegalArgumentException(localizationUtil.getLocalizedMessage(MessageKey.TOKEN_INVALID));
        }
        // Lấy token từ chuỗi login
        String token = login.substring(7).trim();
        if (token == null || token.isEmpty()) {
            log.error("Token is null or empty");
            throw new IllegalArgumentException(localizationUtil.getLocalizedMessage(MessageKey.TOKEN_INVALID));
        }
        if (jwtTokenUtils.isTokenExpired(token)) {
            log.error("Token has expired");
            throw new IllegalArgumentException(localizationUtil.getLocalizedMessage(MessageKey.TOKEN_EXPIRED));
        }  
        String subject = jwtTokenUtils.extractSubject(token);
        log.info("Extracted subject: {}, role: {}", subject, jwtTokenUtils.extractClaim(token, claims -> claims.get("role", String.class)));
        if (token == null || token.isEmpty()) {
            log.error("Invalid or missing token");
            throw new IllegalArgumentException(localizationUtil.getLocalizedMessage(MessageKey.TOKEN_INVALID));
        }
        Optional<User> userOptional;
        if (jwtTokenUtils.extractClaim(token, claims -> claims.get("role", String.class)).equals("CUSTOMER")) {
            userOptional = iUserRepository.findByEmail(subject);
        } else {
            userOptional = iUserRepository.findByUsername(subject);
        }
        UserInforResponse userResponse = modelMapper.map(userOptional, UserInforResponse.class);
        if (userResponse == null) {
            log.error("User not found for token: {}", token);
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND));
        }
        return userResponse;
    }

    @Override
    public APIResponse<Boolean> addAddressByCustomer(String token, AddressCustomerRequest addressCustomerRequest) throws NotFoundException {
        String jwt = token.substring(7).trim();
        if (jwtTokenUtils.isTokenExpired(jwt)) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.TOKEN_EXPIRED));
        }
        String subject = jwtTokenUtils.extractSubject(jwt);
        Optional<User> userOptional;
        if (jwtTokenUtils.extractClaim(jwt, claims -> claims.get("email", String.class)) == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND));
        }
        userOptional = iUserRepository.findByEmail(subject);
        User user = userOptional.get();
        if (user.getAddresses().size() > 2) { // đi từ 0 -> 2 sẽ là 3 địa chỉ
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_ADDRESS_MAXIMUM_LIMIT));            
        }else{
            Address addressByCustomer = Address.builder()
                    .name(addressCustomerRequest.getName())
                    .phoneNumber(addressCustomerRequest.getPhoneNumber())
                    .street(addressCustomerRequest.getStreet())
                    .ward(addressCustomerRequest.getWard())
                    .district(addressCustomerRequest.getDistrict())
                    .city(addressCustomerRequest.getCity())
                    .country(addressCustomerRequest.getCountry())
                    .isDefault(addressCustomerRequest.isDefaultAddress()) // true: địa chỉ này sẽ là địa chỉ mặc định, false: địa chỉ này sẽ không phải là địa chỉ mặc định
                    .type(addressCustomerRequest.isType()) // true: địa chỉ này là địa chỉ nhà, false: địa chỉ này là địa chỉ văn phòng
                    .user(user)
                    .build();
            iAddressRepository.save(addressByCustomer);
            user.getAddresses().add(addressByCustomer);
        }
        iUserRepository.save(user);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_CREATE_ADDRESS_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> updateAddressByCustomer(String token, String addressID, AddressCustomerRequest addressCustomerRequest) throws NotFoundException {
        String jwt = token.substring(7).trim();
        if (jwtTokenUtils.isTokenExpired(jwt)) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.TOKEN_EXPIRED));
        }
        String subject = jwtTokenUtils.extractSubject(jwt);
        Optional<User> userOptional;
        if (jwtTokenUtils.extractClaim(jwt, claims -> claims.get("email", String.class)) == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND));
        }
        Address address = iAddressRepository.findById(UUID.fromString(addressID)).orElse(null);
        if (address == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_ADDRESS_NOT_FOUND));
        }
        userOptional = iUserRepository.findByEmail(subject);
        User user = userOptional.get();
        modelMapper.map(addressCustomerRequest, address);
        address.setUser(user);
        address.setModifiedDate(LocalDateTime.now());
        iAddressRepository.save(address);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_UPDATE_ADDRESS_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> deleteAddressByCustomer(String token, String addressID) throws NotFoundException {
        String jwt = token.substring(7).trim();
        if (jwtTokenUtils.isTokenExpired(jwt)) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.TOKEN_EXPIRED));
        }
        String subject = jwtTokenUtils.extractSubject(jwt);
        Optional<User> userOptional;
        if (jwtTokenUtils.extractClaim(jwt, claims -> claims.get("email", String.class)) == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND));
        }
        Address address = iAddressRepository.findById(UUID.fromString(addressID)).orElse(null);
        if (address == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_ADDRESS_NOT_FOUND));
        }
        userOptional = iUserRepository.findByEmail(subject);
        User user = userOptional.get();
        if (!user.getAddresses().contains(address)) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_ADDRESS_NOT_FOUND));
        }
        iAddressRepository.delete(address);
        user.getAddresses().remove(address);
        iUserRepository.save(user);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_DELETE_ADDRESS_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> updateProfileCustomer(String token, ChangeUserInfoRequest changeUserInfoRequest)
            throws NotFoundException {
        String jwt = token.substring(7).trim();
        if (jwtTokenUtils.isTokenExpired(jwt)) {
                throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.TOKEN_EXPIRED));
        }
        String subject = jwtTokenUtils.extractSubject(jwt);
        Optional<User> userOptional;
        if (jwtTokenUtils.extractClaim(jwt, claims -> claims.get("email", String.class)) == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_FOUND));
        }
        userOptional = iUserRepository.findByEmail(subject);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.USER_NOT_EXIST));
        }
        User user = userOptional.get();
        modelMapper.map(changeUserInfoRequest, user);
        user.setFullName(changeUserInfoRequest.getFirstName() + " " + changeUserInfoRequest.getMiddleName() + " " + changeUserInfoRequest.getLastName());
        String slugStandardization = vietnameseStringUtils.removeAccents(user.getFullName()).toLowerCase();
        user.setSlug(slugStandardization);
        String usernameStandardization = vietnameseStringUtils.removeAccentsAndSpaces(changeUserInfoRequest.getFirstName() + changeUserInfoRequest.getMiddleName() + changeUserInfoRequest.getLastName()).toLowerCase();
        user.setUsername(usernameStandardization + new Random().nextInt(1000));
        Calendar calendar = Calendar.getInstance();
        calendar.set(changeUserInfoRequest.getYear(), changeUserInfoRequest.getMonth() - 1, changeUserInfoRequest.getDay());
        Date birthday = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(birthday);
        try {
            user.setBirthDay(dateFormat.parse(formattedDate));
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format: " + formattedDate, e);
        }
        user.setGender(changeUserInfoRequest.getGender());
        user.setModifiedDate(LocalDateTime.now());
        iUserRepository.save(user);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.USER_UPDATE_PROFILE_SUCCESS));
        return new APIResponse<>(true, messages);
    }
}
