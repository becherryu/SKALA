package com.sk.skala.stockapi.data.table;

import jakarta.validation.constraints.Positive;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Positive(message = "id는 0보다 커야 합니다")
	private Long id;
	private String stockName;
	private Double stockPrice;

	public Stock(String stockName, Double stockPrice) {
		this.stockName = stockName;
		this.stockPrice = stockPrice;
	}
}
