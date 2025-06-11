package com.gentlemonster.GentleMonsterBE.Controllers.Admin;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Role.AddRoleRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Role.EditRoleRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Role.RoleRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.Services.Role.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Enpoint.Role.BASE)
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<PagingResponse<?>> getAllRole(@ModelAttribute RoleRequest roleRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            return ResponseEntity.badRequest().body(new PagingResponse<>(null,  errorMessages, 0, (long) 0));
        }
        return ResponseEntity.ok(roleService.getAllRole(roleRequest));
    }

    @PostMapping(Enpoint.Role.NEW)
    public ResponseEntity<APIResponse<Boolean>> createNewRole(@Valid @RequestBody AddRoleRequest addRoleRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(roleService.addRole(addRoleRequest));
    }


    @PutMapping(Enpoint.Role.EDIT)
    public ResponseEntity<APIResponse<Boolean>> editRole(@PathVariable String roleID,@Valid @RequestBody EditRoleRequest editRoleRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(roleService.editRole(roleID,editRoleRequest));
    }
    @DeleteMapping(Enpoint.Role.DELETE)
    public ResponseEntity<APIResponse<Boolean>> deleteRole(@PathVariable String roleID) {
        return ResponseEntity.ok(roleService.deleteRole(roleID));
    }
}
