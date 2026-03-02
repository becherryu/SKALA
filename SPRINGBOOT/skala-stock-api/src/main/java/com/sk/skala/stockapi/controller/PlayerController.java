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
import com.sk.skala.stockapi.data.dto.PlayerSession;
import com.sk.skala.stockapi.data.dto.StockOrder;
import com.sk.skala.stockapi.data.table.Player;
import com.sk.skala.stockapi.service.PlayerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping("/list")
    public Response getAllPlayers(@RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "count", defaultValue = "10") int count) {
        // 쿼리 파라미터로 offset, count(기본 0 ,10)

        // PlayerService에서 페이징 적용된 플레이어 목록을 받아 반환
        return playerService.getAllPlayers(offset, count);
    }

    // 단일 플레이어 상세 조회 API
    @GetMapping("/{playerId}")
    public Response getPlayerById(@PathVariable String playerId) {
        // 경로변수(@PathVariable)로 playerId 전달

        // 해당 플레이어 정보 + 보유 주식 리스트 반환
        return playerService.getPlayerById(playerId);
    }

    // 플레이어 등록 API
    @PostMapping
    public Response createPlayer(@RequestBody PlayerSession playerSession) {
        // 서비스로 Player 정보 전달
        // 신규 플레이어 등록, Response 반환

        return playerService.createPlayer(playerSession);
    }

    // 플레이어 로그인 API
    @PostMapping("/login")
    public Response loginPlayer(@RequestBody PlayerSession playerSession) {
        // 서비스로 PlayerSession(id, pw) 전달
        // 로그인 성공 시 토큰/세션 부여 및 플레이어 정보 반환

        return playerService.loginPlayer(playerSession);
    }

    // 플레이어 정보 수정 API
    @PutMapping
    public Response updatePlayer(@RequestBody Player player) {
        // 서비스로 수정할 Player 정보 전달
        // 플레이어 정보(주로 자산) 업데이트
        return playerService.updatePlayer(player);
    }

    // 플레이어 삭제 API
    @DeleteMapping
    public Response deletePlayer(@RequestBody Player player) {
        // 서비스로 삭제할 Player 정보 전달 (Id 필요)
        // 해당 플레이어 삭제
        return playerService.deletePlayer(player);
    }

    // 플레이어 주식 매수 API
    @PostMapping("/buy")
    public Response buyPlayerStock(@RequestBody StockOrder order) {
        // 서비스로 StockOrder(stockId, quantity 등) 전달
        // 플레이어가 주식 매수
        return playerService.buyPlayerStock(order);
    }

    // 플레이어 주식 매도 API
    @PostMapping("/sell")
    public Response sellPlayerStock(@RequestBody StockOrder stock) {
        // 서비스로 StockOrder 전달
        // 플레이어가 주식 매도

        return playerService.sellPlayerStock(stock);
    }

}
