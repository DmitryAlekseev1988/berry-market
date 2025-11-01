package com.berry.dto;

import com.berry.entity.AdvertType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertRequest {

    @NotNull(message = "Berry type ID is required")
    @NonNull
    private Long berryId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Double quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    private String address;

    private String description;

    @NotNull(message = "Advert type is required")
    private AdvertType type;
}
