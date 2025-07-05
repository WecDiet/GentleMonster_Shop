package com.gentlemonster.GentleMonsterBE.Controllers.Admin;

import java.util.Collections;
import java.util.List;

import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.ChangePasswordRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Auth.UserLoginRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Auth.UserLoginResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.User.UserInforResponse;
import com.gentlemonster.GentleMonsterBE.Exception.NotFoundException;
import com.gentlemonster.GentleMonsterBE.Services.Auth.AuthService;
import com.gentlemonster.GentleMonsterBE.Services.User.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(Enpoint.Auth.BASE_ADMIN)
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;
    @PostMapping(Enpoint.Auth.LOGIN_ADMIN)
    public ResponseEntity<APIResponse<UserLoginResponse>> employeeLogin(@Valid UserLoginRequest userLoginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new APIResponse<>(null, errorMessages));
        }
        try {
            APIResponse<UserLoginResponse> response = authService.login(
                    userLoginRequest.getLogin(),
                    userLoginRequest.getPassword(),
                    userLoginRequest.getTokenType(),
                    userLoginRequest.getDeviceToken(),
                    userLoginRequest.getDeviceName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(null, Collections.singletonList(e.getMessage())));
        }
    }

    @PostMapping(Enpoint.Auth.CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token, @Valid @RequestBody ChangePasswordRequest changePasswordRequest, BindingResult bindingResult) throws NotFoundException {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new APIResponse<>(null, errorMessages));
        }
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Authorization header");
            }
            String result = authService.changeUserPassword(changePasswordRequest, token);
            return ResponseEntity.ok(result);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>(null, Collections.singletonList(e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(null, Collections.singletonList(e.getMessage())));
        }

    }

    @GetMapping(Enpoint.Auth.ME)
    public ResponseEntity<UserInforResponse> getInforCurrentUser(@RequestHeader("Authorization") String token) throws NotFoundException {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            UserInforResponse userInforResponse = userService.getUserLoginResponse(token);
            return ResponseEntity.ok(userInforResponse);

    }
}
