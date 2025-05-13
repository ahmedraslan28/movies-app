package com.raslan.service.Auth;

import com.raslan.dto.auth.UserResponse;

public interface AuthService {
    UserResponse login(String email, String password);

    UserResponse register(String email, String password, String password2);


}
