package com.gentlemonster.Services.Role;

import com.gentlemonster.DTO.Requests.Role.AddRoleRequest;
import com.gentlemonster.DTO.Requests.Role.EditRoleRequest;
import com.gentlemonster.DTO.Requests.Role.RoleRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.Role.RoleResponse;
import com.gentlemonster.Exception.NotFoundException;

import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public interface IRoleService {
    PagingResponse<List<RoleResponse>> getAllRole(@ModelAttribute RoleRequest roleRequest);
    APIResponse<Boolean> addRole(AddRoleRequest addRoleRequest);
    APIResponse<Boolean> editRole (String roleID, EditRoleRequest editRoleRequest);
    APIResponse<Boolean> deleteRole(String roleID) throws NotFoundException;
}
