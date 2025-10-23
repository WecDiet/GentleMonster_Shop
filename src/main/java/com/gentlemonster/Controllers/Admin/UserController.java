package com.gentlemonster.Controllers.Admin;

import com.gentlemonster.Contants.Enpoint;
import com.gentlemonster.DTO.Requests.User.AddUserResquest;
import com.gentlemonster.DTO.Requests.User.EditUserRequest;
import com.gentlemonster.DTO.Requests.User.UserRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.User.BaseUserResponse;
import com.gentlemonster.DTO.Responses.User.UserResponse;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Services.User.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(Enpoint.User.BASE)
//@Validateds
public class UserController {
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PagingResponse<?>> getAllUser(@ModelAttribute UserRequest userRequest,@Valid BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            logger.error("Error Search user: " + errorMessages);
            return ResponseEntity.badRequest().body(new PagingResponse<>(null,  errorMessages, 0, (long) 0));
        }
        return ResponseEntity.ok(userService.getAllUser(userRequest));
    }

    @GetMapping(Enpoint.User.SEARCH_USER)
    public ResponseEntity<APIResponse<List<BaseUserResponse>>> searchUser(@Valid @ModelAttribute UserRequest userRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            logger.error("Error Search user: " + errorMessages);
            return ResponseEntity.badRequest().body(new APIResponse<>(null,  errorMessages));
        }
        return ResponseEntity.ok(userService.searchUser(userRequest));
    }
    @GetMapping(Enpoint.User.ID)
    public ResponseEntity<APIResponse<UserResponse>> getUserByID(@PathVariable String userID) throws NotFoundException {
        return ResponseEntity.ok(userService.getOneUser(userID));
    }

    @PostMapping(Enpoint.User.NEW)
    public ResponseEntity<APIResponse<Boolean>> createNewUser(@Valid @RequestBody AddUserResquest addUserRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            logger.error("Error creating user: " + errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(userService.addUser(addUserRequest));
    }

    @PutMapping(Enpoint.User.EDIT)
    public ResponseEntity<APIResponse<Boolean>> editUser(@PathVariable String userID, @Valid @RequestBody EditUserRequest editUserRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            APIResponse<Boolean> errorResponse = new APIResponse<>(null, errorMessages);
            logger.error("Error editing user: " + errorMessages);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(userService.updateUser(userID, editUserRequest));
    }

    @DeleteMapping(Enpoint.User.DELETE)
    public ResponseEntity<APIResponse<Boolean>> deleteUser(@PathVariable String userID) throws NotFoundException {
        return ResponseEntity.ok(userService.deleteUser(userID));
    }

    @PostMapping(Enpoint.User.UPLOAD_AVATAR)
    public ResponseEntity<APIResponse<Boolean>> uploadAvatarEmpployee(@PathVariable String userID, @RequestParam("image") MultipartFile image) throws NotFoundException {
        return ResponseEntity.ok(userService.uploadAvatarEmployee(userID, image));
    }
}
