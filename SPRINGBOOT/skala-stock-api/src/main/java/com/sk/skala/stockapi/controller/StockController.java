package com.sk.skala.stockapi.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sk.skala.stockapi.data.common.Response;
import com.sk.skala.stockapi.data.table.Stock;
import com.sk.skala.stockapi.service.StockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    // 전체 주식 목록 조회 API
    @GetMapping("/list")
    public Response getAllStocks(@RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer count) {
        // 쿼리 파라미터로 offset, count(기본 0, 10)

        // Service에서 페이징 적용된 주식 목록을 받아 반환
        return stockService.getAllStocks(offset, count);
    }

    // 개별 주식 상세 조회 API
    @GetMapping("/{id}")
    public Response getStockById(@PathVariable Long id) {
        // 서비스에서 해당 주식 정보 반환
        return stockService.getStockById(id);
    }

    // 주식 등록 API
    @PostMapping
    public Response createStock(@RequestBody Stock stock) {
        // JSON 바디에 주식 정보(Stock 엔터티) 입력
        // 서비스를 통해 새 주식 등록, 결과 반환
        return stockService.createStock(stock);
    }

    // 주식 정보 수정 API
    @PutMapping
    public Response updateStock(@RequestBody Stock stock) {
        // 서비스를 통해 주식 정보 업데이트
        return stockService.updateStock(stock);
    }

    // 주식 삭제 API
    @DeleteMapping
    public Response deleteStock(@RequestBody Stock stock) {
        // 서비스로 삭제할 주식 정보 전달(일반적으로 id만 필요)
        return stockService.deleteStock(stock);
    }

}
