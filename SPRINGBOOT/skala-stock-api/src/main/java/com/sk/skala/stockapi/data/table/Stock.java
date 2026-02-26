package com.sk.skala.stockapi.data.table;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "stock")
@Data
public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String stockName;
	private Double stockPrice;

	protected Stock() {
	}

	public Stock(String stockName, Double stockPrice) {
		this.stockName = stockName;
		this.stockPrice = stockPrice;
	}
}
