package com.sk.skala.stockapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sk.skala.stockapi.config.Error;
import com.sk.skala.stockapi.data.common.PagedList;
import com.sk.skala.stockapi.data.common.Response;
import com.sk.skala.stockapi.data.table.Stock;
import com.sk.skala.stockapi.exception.ParameterException;
import com.sk.skala.stockapi.exception.ResponseException;
import com.sk.skala.stockapi.repository.StockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {
    // Stock 엔터티를 관리하는 저장소
    private final StockRepository stockRepository;

    // 전체 주식 목록 조회
    public Response getAllStocks(int offset, int count) {
        // 페이징 및 정렬을 위한 Pageable 객체 생성
        Pageable pageable = PageRequest.of(offset, count, Sort.by("stockName").ascending());

        // 페이지 단위 데이터 조회
        Page<Stock> stockPage = stockRepository.findAll(pageable);

        // 결과를 PagedList 객체로 가공
        PagedList pagedList = new PagedList();
        pagedList.setTotal(stockPage.getTotalElements());
        pagedList.setCount(stockPage.getNumberOfElements());
        pagedList.setOffset(stockPage.getNumber());
        pagedList.setList(stockPage.getContent());

        // Response 객체에 담아 반환
        return success(pagedList);
    }

    // 개별 주식 상세 조회
    public Response getStockById(Long id) {
        if (id == null) {
            throw new ParameterException("id");
        }

        // ID로 주식(Stock) 조회, Optional로 존재 여부 확인: 없으면
        // ResponseException(Error.DATA_NOT_FOUND)
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

        // Response 객체에 담아 반환
        return success(stock);
    }

    // 주식 등록(생성)
    public Response createStock(Stock stock) {
        // 입력값 검증 (stockName 비어있음 or stockPrice <= 0): 오류시
        // ParameterException("stockName", "stockPrice")
        validateStockInput(stock);

        // 이름 중복 체크(findByStockName): 중복시 ResponseException(Error.DATA_DUPLICATED)
        stockRepository.findByStockName(stock.getStockName())
                .ifPresent(ignored -> {
                    throw new ResponseException(Error.DATA_DUPLICATED);
                });

        // 신규 Stock의 ID는 null로 세팅(=JPA가 자동 생성)
        stock.setId(null);

        // 저장 후 Response 반환
        Stock createdStock = stockRepository.save(stock);
        return success(createdStock);
    }

    // 주식 정보 수정
    public Response updateStock(Stock stock) {
        // 입력값 검증 (stockName, stockPrice): 오류시 ParameterException("stockName",
        // "stockPrice")
        validateStockInput(stock);
        validateStockId(stock);

        // 해당 ID의 Stock이 존재하는지 확인: 없으면 ResponseException(Error.DATA_NOT_FOUND)
        Stock targetStock = stockRepository.findById(stock.getId())
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

        // 이름 중복 체크(본인 제외): 중복시 ResponseException(Error.DATA_DUPLICATED)
        stockRepository.findByStockName(stock.getStockName())
                .ifPresent(foundStock -> {
                    if (!foundStock.getId().equals(stock.getId())) {
                        throw new ResponseException(Error.DATA_DUPLICATED);
                    }
                });

        targetStock.setStockName(stock.getStockName());
        targetStock.setStockPrice(stock.getStockPrice());

        // 수정(저장) 후 Response 반환
        Stock updatedStock = stockRepository.save(targetStock);
        return success(updatedStock);
    }

    // 주식 삭제
    public Response deleteStock(Stock stock) {
        validateStockId(stock);

        // ID로 조회해서 존재하면 삭제, 없으면 예외 처리 : ResponseException(Error.DATA_NOT_FOUND)
        Stock targetStock = stockRepository.findById(stock.getId())
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));
        stockRepository.delete(targetStock);

        // 삭제 후 Response 반환
        return success(targetStock);
    }

    private void validateStockInput(Stock stock) {
        if (stock == null || stock.getStockName() == null || stock.getStockName().trim().isEmpty()
                || stock.getStockPrice() == null || stock.getStockPrice() <= 0) {
            throw new ParameterException("stockName", "stockPrice");
        }
    }

    private void validateStockId(Stock stock) {
        if (stock == null || stock.getId() == null) {
            throw new ParameterException("id");
        }
    }

    private Response success(Object body) {
        Response response = new Response();
        response.setResult(Response.SUCCESS);
        response.setBody(body);
        return response;
    }
}
