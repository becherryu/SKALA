package com.sk.skala.stockapi.data.table;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "player")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {

	@Id
	private String playerId;
	private String playerPassword;
	private Double playerMoney;

	public Player(String playerId, Double playerMoney) {
		this.playerId = playerId;
		this.playerMoney = playerMoney;
	}

}
