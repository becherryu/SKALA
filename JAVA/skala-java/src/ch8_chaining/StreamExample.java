package ch8_chaining;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamExample {

    public static void main(String[] args) {
        List<Stock> stocks = Arrays.asList(
                new Stock("SKALA AI", 15000),
                new Stock("SKALA EDU", 9000),
                new Stock("SKALA LAB", 20000)
        );

        List<String> expensiveStockNames = stocks.stream()
                .filter(stock -> stock.getPrice() >= 10000) // 조건 필터링
                .map(Stock::getName) // 이름만 추출
                .sorted() // 정렬
                .collect(Collectors.toList());                   // 리스트로 수집

        System.out.println(expensiveStockNames); // [SKALA AI, SKALA LAB]
    }

}
