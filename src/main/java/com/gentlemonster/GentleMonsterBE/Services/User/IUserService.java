package com.gentlemonster.GentleMonsterBE.Services.User;

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
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    PagingResponse<List<BaseUserResponse>> getAllUser(@ModelAttribute UserRequest userRequest);
    PagingResponse<List<UserResponse>> getAllUserByRole(@ModelAttribute UserRequest userRequest);
    APIResponse<Boolean> addUser(AddUserResquest addUserRequest) throws NotFoundException;
    APIResponse<UserResponse> getOneUser(String userID) throws NotFoundException;
    APIResponse<Boolean> updateUser(String userID, EditUserRequest editUserRequest) throws NotFoundException;
    APIResponse<Boolean> deleteUser(String userID) throws NotFoundException;
    APIResponse<List<BaseUserResponse>> searchUser(UserRequest userRequest);
    APIResponse<Boolean> deleteMutiUser(List<String> userIDs);
    APIResponse<Boolean> uploadAvatarEmployee(String userID, MultipartFile image) throws NotFoundException;
    UserInforResponse getUserLoginResponse(String login) throws NotFoundException;
    APIResponse<Boolean> addAddressByCustomer(String token, AddressCustomerRequest addressCustomerRequest) throws NotFoundException;
    APIResponse<Boolean> updateAddressByCustomer(String token, String addressID, AddressCustomerRequest addressCustomerRequest) throws NotFoundException;
    APIResponse<Boolean> deleteAddressByCustomer(String token, String addressID, AddressCustomerRequest addressCustomerRequest) throws NotFoundException;
    APIResponse<List<AddressCustomerResponse>> getAllAddressByCustomer(String token) throws NotFoundException;
    APIResponse<Boolean> uploadAvatarCustomer(String token, MultipartFile image) throws NotFoundException;
}
