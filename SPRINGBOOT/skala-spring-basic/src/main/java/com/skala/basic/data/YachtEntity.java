package com.skala.basic.data;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class YachtEntity {

    @NotBlank(message = "요트 이름은 필수입니다.")
    @Size(min = 2, max = 50, message = "요트 이름은 2~50자 사이여야 합니다.")
    private String name;

    @NotBlank(message = "제조사는 필수입니다.")
    private String manufacturer;

    @Min(value = 10, message = "길이는 최소 10ft 이상이어야 합니다.")
    @Max(value = 300, message = "길이는 최대 300ft 이하이어야 합니다.")
    private int lengthFeet;

    @DecimalMin(value = "0.0", inclusive = false, message = "가격은 0보다 커야 합니다.")
    @Digits(integer = 12, fraction = 2, message = "가격 형식이 올바르지 않습니다.")
    private BigDecimal price;

    @NotNull(message = "제조년도는 필수입니다.")
    @PastOrPresent(message = "제조년도는 현재 또는 과거여야 합니다.")
    private LocalDate builtDate;
}
