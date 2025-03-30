package com.gentlemonster.GentleMonsterBE.Services.Auth;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.ChangePasswordRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.ChangeUserInfoRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.UserLoginRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.UserRegisterRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Auth.UserLoginResponse;
import com.gentlemonster.GentleMonsterBE.Entities.User;

public interface IAuthService {
    APIResponse<Boolean> createUser(UserRegisterRequest userRegisterRequest) throws Exception;
    String login(UserLoginRequest userLoginRequest) throws Exception;
    User saveDeviceToken(String email, String deviceToken) throws  Exception;
    String changeUserPassword(ChangePasswordRequest changePassword, String token) throws Exception;
    UserLoginResponse changeUserInfo(ChangeUserInfoRequest changeUserInfo);
    User registerGoogle(String token);
    String loginGoogle(String token, String deviceToken) throws Exception;
    User registerFacebook(String token);
    String loginFacebook(String token, String deviceToken) throws Exception;
}
