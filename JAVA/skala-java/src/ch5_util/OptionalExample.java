package ch5_util;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class OptionalExample {

    public static Optional<Stock> findStockByName(List<Stock> stocks, String name) {
        for (Stock stock : stocks) {
            if (stock.name.equalsIgnoreCase(name)) {
                return Optional.of(stock);
            }
        }
        return Optional.empty();
    }

    public static void main(String[] args) {
        List<Stock> stockList = List.of(
                new Stock("SKALA 에듀", 12000),
                new Stock("SKALA AI", 18000)
        );

        Scanner scanner = new Scanner(System.in);
        System.out.print("조회할 주식명을 입력하세요: ");
        String input = scanner.nextLine().trim();

        Optional<Stock> found = findStockByName(stockList, input);

        found.ifPresentOrElse(
                stock -> System.out.println("현재가: " + stock.price + "원"),
                () -> System.out.println("해당 주식을 찾을 수 없습니다.")
        );
    }

}
