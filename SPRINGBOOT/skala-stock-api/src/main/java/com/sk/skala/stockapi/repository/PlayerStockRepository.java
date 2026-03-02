package com.sk.skala.stockapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.skala.stockapi.data.table.Player;
import com.sk.skala.stockapi.data.table.PlayerStock;
import com.sk.skala.stockapi.data.table.Stock;

public interface PlayerStockRepository extends JpaRepository<PlayerStock, Long> {
    // playerStock 엔터티를 관리하는 저장소 인터페이스

    // playerStock 엔터티의 player 필드가 참조하는 player 엔터티의 playerId 필드 값과 일치하는 playerStock
    // 목록 조회
    List<PlayerStock> findStocksByPlayer(Player player);

    // 특정 player가 특정 stock을 보유하고 있는지 검색
    Optional<PlayerStock> findByPlayerAndStock(Player player, Stock stock);
}
