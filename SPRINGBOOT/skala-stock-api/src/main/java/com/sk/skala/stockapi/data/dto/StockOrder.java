package com.sk.skala.stockapi.data.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class StockOrder {
	private String playerId;

	@Positive(message = "stockId는 0보다 커야 합니다")
	private long stockId;

	@Positive(message = "stockQuantity는 0보다 커야 합니다")
	private int stockQuantity;
}
