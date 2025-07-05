package com.gentlemonster.GentleMonsterBE.Services.Auth;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.ChangePasswordRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.ChangeUserInfoRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.UserRegisterRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Auth.UserLoginResponse;
import com.gentlemonster.GentleMonsterBE.Entities.AuthToken;
import com.gentlemonster.GentleMonsterBE.Entities.User;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;


public interface IAuthService {
    APIResponse<Boolean> createUser(UserRegisterRequest userRegisterRequest) throws Exception;
    APIResponse<UserLoginResponse> login(String login, String password, String tokenType,String deviceToken, String deviceName) throws Exception;
    String changeUserPassword(ChangePasswordRequest changePassword, String token) throws NotFoundException;
    UserLoginResponse changeUserInfo(ChangeUserInfoRequest changeUserInfo);
    User registerGoogle(String token);
    String loginGoogle(String token, String deviceToken) throws Exception;
    User registerFacebook(String token);
    String loginFacebook(String token, String deviceToken) throws Exception;
    
}
