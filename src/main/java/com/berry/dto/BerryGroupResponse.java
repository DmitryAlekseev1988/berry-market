package com.berry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BerryGroupResponse {

  private Long id;
  private String name;
  private String description;
}
