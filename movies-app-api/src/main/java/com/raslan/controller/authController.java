package com.raslan.controller;


import com.raslan.dto.auth.LoginRequest;
import com.raslan.dto.auth.RegisterRequest;
import com.raslan.dto.auth.UserResponse;
import com.raslan.service.Auth.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class authController {
    private final AuthService authService ;
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse userResponse = authService.register(request.getEmail(), request.getPassword(), request.getPassword2());
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request){
        UserResponse userResponse = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(userResponse);
    }




}
