package com.sk.skala.stockapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.skala.stockapi.data.table.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
    // 주식 데이터 저장소

    // 주식 이름과 동일한 주식 데이터를 조회하는 메서드
    // 존재 여부 확인 목적(조회 결과가 없을 수 있는 단건 조회) -> Optional
    Optional<Stock> findByStockName(String stockName);
}
