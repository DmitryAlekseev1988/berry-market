package com.berry.dto;

import com.berry.entity.AdvertType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

  @Positive(message = "Berry ID must be positive")
  private Long berryId;

  @Positive(message = "Berry group ID must be positive")
  private Long berryGroupId;

  @DecimalMin(value = "0.0", inclusive = true, message = "Min price must be greater than or equal to 0")
  private BigDecimal minPrice;

  @DecimalMin(value = "0.0", inclusive = true, message = "Max price must be greater than or equal to 0")
  private BigDecimal maxPrice;

  private AdvertType advertType;

  @AssertTrue(message = "Min price must be less than or equal to max price")
  private boolean isPriceRangeValid() {
    if (minPrice != null && maxPrice != null) {
      return minPrice.compareTo(maxPrice) <= 0;
    }
    return true;
  }
}
