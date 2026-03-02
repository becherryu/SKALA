package com.sk.skala.stockapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import com.sk.skala.stockapi.data.common.Response;
import com.sk.skala.stockapi.data.table.Stock;
import com.sk.skala.stockapi.service.StockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Stock", description = "주식 관리 API")
public class StockController {
    private final StockService stockService;

	// 전체 주식 목록 조회 API
	@GetMapping("/list")
	@Operation(summary = "주식 목록 조회", description = "페이징 조건으로 전체 주식 목록을 조회합니다.")
	public Response getAllStocks(@RequestParam(defaultValue = "0") @Min(value = 0, message = "offset은 0 이상이어야 합니다") Integer offset,
			@RequestParam(defaultValue = "10") @Positive(message = "count는 0보다 커야 합니다") Integer count) {
        // 쿼리 파라미터로 offset, count(기본 0, 10)

        // Service에서 페이징 적용된 주식 목록을 받아 반환
        return stockService.getAllStocks(offset, count);
    }

	// 개별 주식 상세 조회 API
	@GetMapping("/{id}")
	@Operation(summary = "주식 단건 조회", description = "주식 ID로 단일 주식 정보를 조회합니다.")
	public Response getStockById(@PathVariable @Positive(message = "id는 0보다 커야 합니다") Long id) {
        // 서비스에서 해당 주식 정보 반환
        return stockService.getStockById(id);
    }

	// 주식 등록 API
	@PostMapping
	@Operation(summary = "주식 생성", description = "신규 주식 정보를 생성합니다.")
	public Response createStock(@RequestBody @Valid Stock stock) {
        // JSON 바디에 주식 정보(Stock 엔터티) 입력
        // 서비스를 통해 새 주식 등록, 결과 반환
        return stockService.createStock(stock);
    }

	// 주식 정보 수정 API
	@PutMapping
	@Operation(summary = "주식 수정", description = "기존 주식 정보를 수정합니다.")
	public Response updateStock(@RequestBody @Valid Stock stock) {
        // 서비스를 통해 주식 정보 업데이트
        return stockService.updateStock(stock);
    }

	// 주식 삭제 API
	@DeleteMapping
	@Operation(summary = "주식 삭제", description = "주식 정보를 삭제합니다.")
	public Response deleteStock(@RequestBody @Valid Stock stock) {
        // 서비스로 삭제할 주식 정보 전달(일반적으로 id만 필요)
        return stockService.deleteStock(stock);
    }

}
