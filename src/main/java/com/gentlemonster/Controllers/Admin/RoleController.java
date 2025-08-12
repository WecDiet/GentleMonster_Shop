package com.gentlemonster.Controllers.Admin;

import com.gentlemonster.Contants.Enpoint;
import com.gentlemonster.DTO.Requests.Role.AddRoleRequest;
import com.gentlemonster.DTO.Requests.Role.EditRoleRequest;
import com.gentlemonster.DTO.Requests.Role.RoleRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Services.Role.RoleService;
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
    public ResponseEntity<APIResponse<Boolean>> deleteRole(@PathVariable String roleID) throws NotFoundException {
        return ResponseEntity.ok(roleService.deleteRole(roleID));
    }
}
