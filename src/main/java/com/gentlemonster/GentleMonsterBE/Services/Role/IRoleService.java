package com.gentlemonster.GentleMonsterBE.Services.Role;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Role.AddRoleRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Role.EditRoleRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Role.RoleRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Role.RoleResponse;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;

import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public interface IRoleService {
    PagingResponse<List<RoleResponse>> getAllRole(@ModelAttribute RoleRequest roleRequest);
    APIResponse<Boolean> addRole(AddRoleRequest addRoleRequest);
    APIResponse<Boolean> editRole (String roleID, EditRoleRequest editRoleRequest);
    APIResponse<Boolean> deleteRole(String roleID) throws NotFoundException;
}
