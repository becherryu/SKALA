package com.sk.skala.stockapi.data.table;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "player")
@Data
public class Player {

	@Id
	private String playerId;
	private String playerPassword;
	private Double playerMoney;

	protected Player() {
	}

	public Player(String playerId, Double playerMoney) {
		this.playerId = playerId;
		this.playerMoney = playerMoney;
	}

}
