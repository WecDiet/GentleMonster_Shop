package com.gentlemonster.GentleMonsterBE.Services.Auth;

import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.ChangePasswordRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.ChangeUserInfoRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.UserLoginRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.UserRegisterRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Auth.UserLoginResponse;
import com.gentlemonster.GentleMonsterBE.Entities.Role;
import com.gentlemonster.GentleMonsterBE.Entities.User;
import com.gentlemonster.GentleMonsterBE.Repositories.IAuthRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IRoleRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IUserRepository;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtil;
import com.gentlemonster.GentleMonsterBE.Utils.VietnameseStringUtils;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@NoArgsConstructor
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
    private LocalizationUtil localizationUtil;
    @Autowired
    private VietnameseStringUtils vietnameseStringUtils;

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
                    .orElseThrow(() -> new RuntimeException("Role 'CUSTOMER' not found in database!"));

            // Mã hóa mật khẩu trước khi lưu
            String encodedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());

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
                    .slug(vietnameseStringUtils.removeAccents(userRegisterRequest.getFirstName() + "+" +userRegisterRequest.getMiddleName() +"+"+ userRegisterRequest.getLastName()).toLowerCase().trim())
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
    public String login(UserLoginRequest userLoginRequest) throws Exception {
        return "";
    }

    @Override
    public User saveDeviceToken(String email, String deviceToken) throws Exception {
        return null;
    }

    @Override
    public String changeUserPassword(ChangePasswordRequest changePassword, String token) throws Exception {
        return "";
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
}
