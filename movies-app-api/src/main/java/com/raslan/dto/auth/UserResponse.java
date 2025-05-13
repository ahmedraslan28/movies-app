package com.raslan.dto.auth;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserResponse {
    private Integer userId;
    private String email;
    private String role;
    private String token;
}
