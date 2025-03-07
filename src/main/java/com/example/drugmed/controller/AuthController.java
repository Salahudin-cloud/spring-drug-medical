package com.example.drugmed.controller;

import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.user.UserLoginRequest;
import com.example.drugmed.dto.user.UserRegisterRequest;
import com.example.drugmed.dto.user.UserResponse;
import com.example.drugmed.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> register(@Valid @RequestBody UserRegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> login(@Valid @RequestBody UserLoginRequest request, HttpSession session) {
        return authService.login(request);
    }


    @GetMapping(
            path = "/unit",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<UserResponse>> getAllByUnitType(@RequestParam String unit_type) {
        return authService.getAllUserByUnitType(unit_type);
    }

    @PostMapping(
            path = "/approve",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> approveLogin(@RequestParam Long user_id) {
        return authService.loginApprove(user_id);
    }

    @PostMapping(
            path = "/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> logout() {
        return authService.logout();
    }

    @GetMapping(
            path = "/current-user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> getCurrent() {
        return authService.getCurrent();
    }

}
