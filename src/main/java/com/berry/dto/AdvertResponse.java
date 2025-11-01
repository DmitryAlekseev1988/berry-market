package com.berry.dto;

import com.berry.entity.AdvertStatus;
import com.berry.entity.AdvertType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertResponse {

    private Long id;
    private BerryResponse berry;
    private Double quantity;
    private BigDecimal price;
    private String address;
    private String description;
    private AdvertType advertType;
    private AdvertStatus status;
    private UserResponse user;
    private LocalDateTime createdAt;
}
