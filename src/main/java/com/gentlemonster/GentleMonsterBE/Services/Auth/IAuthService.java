package com.gentlemonster.GentleMonsterBE.Services.Auth;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.ChangePasswordRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.ChangeUserInfoRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.UserRegisterRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.User.AddressCustomerRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Address.AddressCustomerResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Auth.UserLoginResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.User.UserInforResponse;
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
    APIResponse<List<AddressCustomerResponse>> getAllAddressByCustomer(String token) throws NotFoundException;
    APIResponse<Boolean> uploadAvatarCustomer(String token, MultipartFile image) throws NotFoundException;
    APIResponse<Boolean> verifyPassword(String token, String password) throws NotFoundException;

    APIResponse<Boolean> addAddressByCustomer(String token, AddressCustomerRequest addressCustomerRequest) throws NotFoundException;
    APIResponse<Boolean> updateAddressByCustomer(String token, String addressID, AddressCustomerRequest addressCustomerRequest) throws NotFoundException;
    APIResponse<Boolean> deleteAddressByCustomer(String token, String addressID) throws NotFoundException;
    UserInforResponse getUserLoginResponse(String login) throws NotFoundException;
    APIResponse<Boolean> updateProfileCustomer(String token, ChangeUserInfoRequest changeUserInfoRequest) throws NotFoundException;
}
