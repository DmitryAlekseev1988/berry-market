package com.berry.dto;

import com.berry.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private Long userId;
    private String userName;
    private UserType userType;
}
