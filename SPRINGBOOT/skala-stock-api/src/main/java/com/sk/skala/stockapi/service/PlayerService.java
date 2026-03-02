package com.sk.skala.stockapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.skala.stockapi.config.Error;
import com.sk.skala.stockapi.data.common.PagedList;
import com.sk.skala.stockapi.data.common.Response;
import com.sk.skala.stockapi.data.dto.PlayerSession;
import com.sk.skala.stockapi.data.dto.PlayerStockDto;
import com.sk.skala.stockapi.data.dto.PlayerStockListDto;
import com.sk.skala.stockapi.data.dto.StockOrder;
import com.sk.skala.stockapi.data.table.Player;
import com.sk.skala.stockapi.data.table.PlayerStock;
import com.sk.skala.stockapi.data.table.Stock;
import com.sk.skala.stockapi.exception.ParameterException;
import com.sk.skala.stockapi.exception.ResponseException;
import com.sk.skala.stockapi.repository.PlayerRepository;
import com.sk.skala.stockapi.repository.PlayerStockRepository;
import com.sk.skala.stockapi.repository.StockRepository;
import com.sk.skala.stockapi.tools.StringTool;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerService {
	// 클래스 선언 및 필드 정의
	private final StockRepository stockRepository;
	private final PlayerRepository playerRepository;
	private final PlayerStockRepository playerStockRepository;
	private final SessionHandler sessionHandler;

	// 전체 플레이어 목록 조회
	public Response getAllPlayers(int offset, int count) {
		// Pageable 객체 생성 (페이징네이션)
		Pageable pageable = PageRequest.of(offset, count, Sort.by("playerId").ascending());

		// playerRepository.findAll()로 페이지 단위 조회
		Page<Player> playerPage = playerRepository.findAll(pageable);

		// PagedList로 결과 변환 후 Response로 감싸 반환
		PagedList pagedList = new PagedList();
		pagedList.setTotal(playerPage.getTotalElements());
		pagedList.setCount(playerPage.getNumberOfElements());
		pagedList.setOffset(playerPage.getNumber());
		pagedList.setList(playerPage.getContent().stream().map(this::toResponsePlayer).collect(Collectors.toList()));
		return success(pagedList);
	}

	// 단일 플레이어 및 주식 목록 조회
	@Transactional(readOnly = true)
	public Response getPlayerById(String playerId) {
		// Player 엔터티 존재 여부 검증, 예외 처리 (ResponseException(Error.DATA_NOT_FOUND, "Player
		// not found"))
		Player player = playerRepository.findById(playerId)
				.orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND, "Player not found"));

		// 플레이어가 보유한 PlayerStock 리스트 조회
		List<PlayerStock> playerStocks = playerStockRepository.findStocksByPlayer(player);

		// Stream API로 DTO 리스트 변환
		List<PlayerStockDto> stockDtos = playerStocks.stream()
				.map(playerStock -> PlayerStockDto.builder()
						.stockId(playerStock.getStock().getId())
						.stockName(playerStock.getStock().getStockName())
						.stockPrice(playerStock.getStock().getStockPrice())
						.quantity(playerStock.getQuantity())
						.build())
				.collect(Collectors.toList());

		// DTO를 Response에 세팅해 반환
		PlayerStockListDto body = PlayerStockListDto.builder()
				.playerId(player.getPlayerId())
				.playerMoney(player.getPlayerMoney())
				.stocks(stockDtos)
				.build();
		return success(body);
	}

	// 플레이어 생성
	public Response createPlayer(PlayerSession playerSession) {
		// 입력 값 검증(isAnyEmpty): 오류시 ParameterException("playerId", "playerPassword")
		if (playerSession == null
				|| StringTool.isAnyEmpty(playerSession.getPlayerId(), playerSession.getPlayerPassword())) {
			throw new ParameterException("playerId", "playerPassword");
		}

		// 중복 아이디 체크: 오류시 ResponseException(Error.DATA_DUPLICATED)
		playerRepository.findById(playerSession.getPlayerId()).ifPresent(player -> {
			throw new ResponseException(Error.DATA_DUPLICATED);
		});

		// Player 객체 생성, 초기 자산 세팅
		Player newPlayer = new Player(playerSession.getPlayerId(), 1_000_000D);
		newPlayer.setPlayerPassword(playerSession.getPlayerPassword());

		// 저장 후 Response 반환
		Player createdPlayer = playerRepository.save(newPlayer);
		return success(toResponsePlayer(createdPlayer));
	}

	// 플레이어 로그인
	public Response loginPlayer(PlayerSession playerSession) {
		// 입력 값 검증 : 오류시 ParameterException("playerId", "playerPassword")
		if (playerSession == null
				|| StringTool.isAnyEmpty(playerSession.getPlayerId(), playerSession.getPlayerPassword())) {
			throw new ParameterException("playerId", "playerPassword");
		}

		// ID로 조회, 비밀번호 검증 : 오류시 ResponseException(Error.DATA_NOT_FOUND),
		// (Error.NOT_AUTHENTICATED)
		Player player = playerRepository.findById(playerSession.getPlayerId())
				.orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));
		if (!playerSession.getPlayerPassword().equals(player.getPlayerPassword())) {
			throw new ResponseException(Error.NOT_AUTHENTICATED);
		}

		// 인증 성공시 sessionHandler.storeAccessToken 호출
		PlayerSession session = new PlayerSession();
		session.setPlayerId(player.getPlayerId());
		session.setPlayerPassword(player.getPlayerPassword());
		sessionHandler.storeAccessToken(session);

		// 플레이어 정보 Response의 body에 담아 반환 (패스워드 null 처리)
		return success(toResponsePlayer(player));
	}

	// 플레이어 정보 업데이트
	public Response updatePlayer(Player player) {
		// playerId와 playerMoney 유효성 체크 : 오류시 ResponseException(Error.DATA_NOT_FOUND)
		if (player == null || StringTool.isEmpty(player.getPlayerId()) || player.getPlayerMoney() == null) {
			throw new ResponseException(Error.DATA_NOT_FOUND);
		}

		// 존재 확인 후, 자산 업데이트
		Player targetPlayer = playerRepository.findById(player.getPlayerId())
				.orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));
		targetPlayer.setPlayerMoney(player.getPlayerMoney());

		// 저장 후 Response 반환
		Player updatedPlayer = playerRepository.save(targetPlayer);
		return success(toResponsePlayer(updatedPlayer));
	}

	// 플레이어 삭제
	@Transactional
	public Response deletePlayer(Player player) {
		// playerId로 존재 확인 후 삭제 : 오류시 ResponseException(Error.DATA_NOT_FOUND)
		if (player == null || StringTool.isEmpty(player.getPlayerId())) {
			throw new ResponseException(Error.DATA_NOT_FOUND);
		}
		Player targetPlayer = playerRepository.findById(player.getPlayerId())
				.orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

		// FK 제약조건으로 인해 player_stock 자식 레코드를 먼저 삭제
		List<PlayerStock> playerStocks = playerStockRepository.findStocksByPlayer(targetPlayer);
		if (!playerStocks.isEmpty()) {
			playerStockRepository.deleteAll(playerStocks);
		}
		playerRepository.delete(targetPlayer);

		// 저장 후 Response 반환
		return success(toResponsePlayer(targetPlayer));
	}

	// 주식 매수
	@Transactional
	public Response buyPlayerStock(StockOrder order) {
		// 현재 로그인된 playerId 가져오기
		validateOrder(order);
		String playerId = sessionHandler.getPlayerId();

		// player, stock 엔터티 조회 및 검증 - 오류 시 ResponseException(Error.DATA_NOT_FOUND)
		Player player = playerRepository.findById(playerId)
				.orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));
		Stock stock = stockRepository.findById(order.getStockId())
				.orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

		// 잔액 충분성 체크 및 차감 - 오류 시 ResponseException(Error.INSUFFICIENT_FUNDS)
		double totalPrice = stock.getStockPrice() * order.getStockQuantity();
		if (player.getPlayerMoney() < totalPrice) {
			throw new ResponseException(Error.INSUFFICIENT_FUNDS);
		}
		player.setPlayerMoney(player.getPlayerMoney() - totalPrice);

		// PlayerStock에 이미 보유한 주식이면 수량 추가 없으면 추가 생성
		PlayerStock playerStock = playerStockRepository.findByPlayerAndStock(player, stock)
				.map(found -> {
					found.setQuantity(found.getQuantity() + order.getStockQuantity());
					return found;
				})
				.orElseGet(() -> new PlayerStock(player, stock, order.getStockQuantity()));
		playerStockRepository.save(playerStock);
		playerRepository.save(player);

		// 저장 후 Response 반환
		return success(getPlayerById(playerId).getBody());
	}

	// 주식 매도
	@Transactional
	public Response sellPlayerStock(StockOrder order) {
		validateOrder(order);

		// 매도할 Player, Stock 엔터티 조회 - 오류 시 ResponseException(Error.DATA_NOT_FOUND)
		String playerId = sessionHandler.getPlayerId();
		Player player = playerRepository.findById(playerId)
				.orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));
		Stock stock = stockRepository.findById(order.getStockId())
				.orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

		// PlayerStock 보유 수량 검증 - 오류 시 ResposeExceptional(Error.INSUFFICIENT_QUANTITY)
		PlayerStock playerStock = playerStockRepository.findByPlayerAndStock(player, stock)
				.orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));
		if (playerStock.getQuantity() < order.getStockQuantity()) {
			throw new ResponseException(Error.INSUFFICIENT_QUANTITY);
		}

		// 수량 감소 또는 삭제 처리
		int remainQuantity = playerStock.getQuantity() - order.getStockQuantity();
		if (remainQuantity > 0) {
			playerStock.setQuantity(remainQuantity);
			playerStockRepository.save(playerStock);
		} else {
			playerStockRepository.delete(playerStock);
		}

		// 매도 금액 만큼 플레이어 자산 증가
		double sellAmount = stock.getStockPrice() * order.getStockQuantity();
		player.setPlayerMoney(player.getPlayerMoney() + sellAmount);
		playerRepository.save(player);

		// 저장 후 Response 반환
		return success(getPlayerById(playerId).getBody());
	}

	// 비밀번호 제거 응답용 Player 객체 변환
	private Player toResponsePlayer(Player player) {
		Player responsePlayer = new Player(player.getPlayerId(), player.getPlayerMoney());
		responsePlayer.setPlayerPassword(null);
		return responsePlayer;
	}

	// Response 객체 생성 메서드
	private Response success(Object body) {
		Response response = new Response();
		response.setResult(Response.SUCCESS);
		response.setBody(body);
		return response;
	}

	private void validateOrder(StockOrder order) {
		if (order == null || order.getStockId() <= 0 || order.getStockQuantity() <= 0) {
			throw new ParameterException("stockId", "stockQuantity");
		}
	}
}
