package com.sk.skala.stockapi.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStockDto {
    // Player가 보유하는 주식 정보
    private Long stockId;
    private String stockName;
    private Double stockPrice;
    private Integer quantity;
}