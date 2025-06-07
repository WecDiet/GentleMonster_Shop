package com.gentlemonster.GentleMonsterBE.Services.User;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.User.AddUserResquest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.User.EditUserRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.User.UserRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.User.BaseUserResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.User.UserResponse;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public interface IUserService {
    PagingResponse<List<BaseUserResponse>> getAllUser(@ModelAttribute UserRequest userRequest);
    PagingResponse<List<UserResponse>> getAllUserByRole(@ModelAttribute UserRequest userRequest);
    APIResponse<Boolean> addUser(AddUserResquest addUserRequest);
    APIResponse<UserResponse> getOneUser(String userID);
    APIResponse<Boolean> updateUser(String userID, EditUserRequest editUserRequest);
    APIResponse<Boolean> deleteUser(String userID);
    APIResponse<List<BaseUserResponse>> searchUser(UserRequest userRequest);
    APIResponse<Boolean> deleteMutiUser(List<String> userIDs);

}
