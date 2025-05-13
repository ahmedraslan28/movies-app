package com.raslan.service.Auth;

import com.raslan.dto.auth.UserResponse;
import com.raslan.exception.DuplicatedRowException;
import com.raslan.exception.RequestValidationException;
import com.raslan.model.Role;
import com.raslan.model.User;
import com.raslan.repository.UserRepository;
import com.raslan.security.jwt.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    @Override
    public UserResponse login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = (User) authentication.getPrincipal();
        List<String> roles = user.getAuthorities().stream().map(Object::toString).toList();

        String token = jwtUtil.issueToken(email, Map.of("roles",roles));

        return new UserResponse(user.getId(), user.getEmail(), user.getRole().name(), token);
    }

    @Override
    public UserResponse register(String email, String password, String password2) {
        if(userRepository.findByEmail(email).isPresent()){
            throw new DuplicatedRowException("Email already exists");
        }
        if(!password.equals(password2)){
            throw new RequestValidationException("Passwords do not match");
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();

        List<String> roles = user.getAuthorities().stream().map(Object::toString).toList();

        String token = jwtUtil.issueToken(email, Map.of("roles",roles));

        user = userRepository.save(user);

        return new UserResponse(user.getId(), user.getEmail(), user.getRole().name(), token);
    }
}
