package com.gentlemonster.GentleMonsterBE.Services.Role;

import com.gentlemonster.GentleMonsterBE.Contants.MessageKey;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Role.AddRoleRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Role.EditRoleRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Role.RoleRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Role.RoleResponse;
import com.gentlemonster.GentleMonsterBE.Entities.Role;
import com.gentlemonster.GentleMonsterBE.Repositories.IRoleRepository;
import com.gentlemonster.GentleMonsterBE.Utils.LocalizationUtil;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@NoArgsConstructor
public class RoleService implements IRoleService{
    @Autowired
    private IRoleRepository iRoleRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LocalizationUtil localizationUtil;

    @Override
    public PagingResponse<List<RoleResponse>> getAllRole(RoleRequest roleRequest) {
        List<RoleResponse> roleResponses;
        List<Role> roles;
        Pageable pageable;
        if (roleRequest.getPage() == 0 && roleRequest.getLimit() == 0){
            roles = iRoleRepository.findAll();
            roleResponses = roles.stream()
                    .map(role -> modelMapper.map(role, RoleResponse.class))
                    .toList();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.ROLE_GET_SUCCESS));
            return new PagingResponse<>(roleResponses, messages, 1, (long) roleResponses.size());
        }else{
            roleRequest.setPage(Math.max(roleRequest.getPage(), 1));
            pageable = PageRequest.of(roleRequest.getPage() - 1, roleRequest.getLimit());
        }
        Page<Role> rolePage = iRoleRepository.findAll(pageable);
        roles = rolePage.getContent();
        roleResponses = roles.stream()
                .map(role -> modelMapper.map(role, RoleResponse.class))
                .toList();
        return new PagingResponse<>(roleResponses, null, rolePage.getTotalPages(), rolePage.getTotalElements());
    }

//    @Override
//    public PagingResponse<List<RoleResponse>> getAllRole(RoleRequest roleRequest) {
//        List<RoleResponse> roleResponses;
//        List<Role> roles;
//        Pageable pageable;
//
//        int page = Math.max(roleRequest.getPage() - 1, 0); // Page index should start from 0
//        int size = roleRequest.getLimit() > 0 ? roleRequest.getLimit() : 10; // Default size is 10 if limit is not provided
//        pageable = PageRequest.of(page, size, Sort.by("name").ascending());
//
//        Page<Role> rolePage = iRoleRepository.findAll(pageable);
//        roles = rolePage.getContent();
//        roleResponses = roles.stream()
//                .map(role -> modelMapper.map(role, RoleResponse.class))
//                .toList();
//        List<String> messages = new ArrayList<>();
//        messages.add(localizationUtil.getLocalizedMessage(MessageKey.ROLE_GET_SUCCESS));
//        return new PagingResponse<>(roleResponses, messages, rolePage.getTotalPages(), rolePage.getTotalElements());
//    }

    @Override
    @Transactional
    public APIResponse<Boolean> addRole(AddRoleRequest addRoleRequest) {
        if(addRoleRequest.getName() == null || addRoleRequest.getName().isEmpty()){
            return new APIResponse<>(null, List.of("Role name is required"));
        }
        if (addRoleRequest.getName() != null && !addRoleRequest.getName().isEmpty()) {
            if (iRoleRepository.existsByName(addRoleRequest.getName())) {
                return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.ROLE_EXISTED)));
            }
        }
        Role role = modelMapper.map(addRoleRequest, Role.class);
        role.setName(addRoleRequest.getName());
        role.setDescription(addRoleRequest.getDescription());
        ArrayList<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.ADD_ROLE_SUCCESS));
        iRoleRepository.save(role);
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> editRole(String roleID, EditRoleRequest editRoleRequest) {
        Role role = iRoleRepository.findById(UUID.fromString(roleID)).orElse(null);
        if (role == null){
            return new APIResponse<>(null, List.of(localizationUtil.getLocalizedMessage(MessageKey.ROLE_NOT_EXIST)));
        }
        if (editRoleRequest.getName() == null || editRoleRequest.getName().isEmpty()){
            return new APIResponse<>(null, List.of("Role name is required"));
        }
        modelMapper.map(editRoleRequest, role);
        role.setName(editRoleRequest.getName());
        role.setDescription(editRoleRequest.getDescription());
        role.setModifiedDate(LocalDateTime.now());
        iRoleRepository.save(role);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.EDIT_ROLE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> deleteRole(String roleID) {
        Role role = iRoleRepository.findById(UUID.fromString(roleID)).orElse(null);
        if (role == null){
            return new APIResponse<>(false, List.of(localizationUtil.getLocalizedMessage(MessageKey.ROLE_NOT_FOUND)));
        }
        iRoleRepository.delete(role);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.DELETE_ROLE_SUCCESS));
        return new APIResponse<>(true, messages);
    }
}
