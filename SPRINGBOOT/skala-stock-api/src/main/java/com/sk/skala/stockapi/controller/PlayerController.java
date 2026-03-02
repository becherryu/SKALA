package com.sk.skala.stockapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
import com.sk.skala.stockapi.data.dto.PlayerSession;
import com.sk.skala.stockapi.data.dto.StockOrder;
import com.sk.skala.stockapi.data.table.Player;
import com.sk.skala.stockapi.service.PlayerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/players")
@Validated
@Tag(name = "Player", description = "플레이어 관리 API")
public class PlayerController {
    private final PlayerService playerService;

	@GetMapping("/list")
	@Operation(summary = "플레이어 목록 조회", description = "페이징 조건으로 전체 플레이어 목록을 조회합니다.")
	public Response getAllPlayers(
			@RequestParam(value = "offset", defaultValue = "0") @Min(value = 0, message = "offset은 0 이상이어야 합니다") int offset,
			@RequestParam(value = "count", defaultValue = "10") @Positive(message = "count는 0보다 커야 합니다") int count) {
        // 쿼리 파라미터로 offset, count(기본 0 ,10)

        // PlayerService에서 페이징 적용된 플레이어 목록을 받아 반환
        return playerService.getAllPlayers(offset, count);
    }

	// 단일 플레이어 상세 조회 API
	@GetMapping("/{playerId}")
	@Operation(summary = "플레이어 단건 조회", description = "플레이어 ID로 플레이어 정보와 보유 주식 목록을 조회합니다.")
	public Response getPlayerById(@PathVariable @NotBlank(message = "playerId는 필수입니다") String playerId) {
        // 경로변수(@PathVariable)로 playerId 전달

        // 해당 플레이어 정보 + 보유 주식 리스트 반환
        return playerService.getPlayerById(playerId);
    }

	// 플레이어 등록 API
	@PostMapping
	@Operation(summary = "플레이어 생성", description = "신규 플레이어를 생성합니다.")
	public Response createPlayer(@RequestBody @Valid PlayerSession playerSession) {
        // 서비스로 Player 정보 전달
        // 신규 플레이어 등록, Response 반환

        return playerService.createPlayer(playerSession);
    }

	// 플레이어 로그인 API
	@PostMapping("/login")
	@Operation(summary = "플레이어 로그인", description = "플레이어 인증 후 세션 토큰을 발급합니다.")
	public Response loginPlayer(@RequestBody @Valid PlayerSession playerSession) {
        // 서비스로 PlayerSession(id, pw) 전달
        // 로그인 성공 시 토큰/세션 부여 및 플레이어 정보 반환

        return playerService.loginPlayer(playerSession);
    }

	// 플레이어 정보 수정 API
	@PutMapping
	@Operation(summary = "플레이어 수정", description = "플레이어 자산 정보를 수정합니다.")
	public Response updatePlayer(@RequestBody @Valid Player player) {
        // 서비스로 수정할 Player 정보 전달
        // 플레이어 정보(주로 자산) 업데이트
        return playerService.updatePlayer(player);
    }

	// 플레이어 삭제 API
	@DeleteMapping
	@Operation(summary = "플레이어 삭제", description = "플레이어 정보를 삭제합니다.")
	public Response deletePlayer(@RequestBody @Valid Player player) {
        // 서비스로 삭제할 Player 정보 전달 (Id 필요)
        // 해당 플레이어 삭제
        return playerService.deletePlayer(player);
    }

	// 플레이어 주식 매수 API
	@PostMapping("/buy")
	@Operation(summary = "주식 매수", description = "현재 로그인 플레이어의 주식을 매수합니다.")
	public Response buyPlayerStock(@RequestBody @Valid StockOrder order) {
        // 서비스로 StockOrder(stockId, quantity 등) 전달
        // 플레이어가 주식 매수
        return playerService.buyPlayerStock(order);
    }

	// 플레이어 주식 매도 API
	@PostMapping("/sell")
	@Operation(summary = "주식 매도", description = "현재 로그인 플레이어의 주식을 매도합니다.")
	public Response sellPlayerStock(@RequestBody @Valid StockOrder stock) {
        // 서비스로 StockOrder 전달
        // 플레이어가 주식 매도

        return playerService.sellPlayerStock(stock);
    }

}
