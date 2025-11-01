package com.berry.dto;

import com.berry.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

  private Long id;
  private String email;
  private String name;
  private UserType userType;
}
