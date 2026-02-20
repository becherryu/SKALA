package ch5_util;

import java.util.Scanner;

public class ScannerExample {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("매수할 주식 이름을 입력하세요: ");
            String stockName = scanner.nextLine();

            System.out.print("매수할 수량을 입력하세요: ");
            int quantity = scanner.nextInt();

            System.out.println("✅ [" + stockName + "] 주식을 " + quantity + "주 매수 요청합니다.");

            scanner.close();
        }
    }

}
