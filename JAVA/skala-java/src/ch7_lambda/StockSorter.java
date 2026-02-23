package ch7_lambda;

public class StockSorter {
    // # 정렬 전 주식 목록:
    // SKALA 에듀
    // SKALA AI
    // K-테크
    // SKALA 테크
    // N-솔루션

    // # 정렬 후 주식 목록:
    // 알파벳 순으로 정렬
    // K-테크
    // N-솔루션
    // SKALA AI
    // SKALA 에듀
    // SKALA 테크
    public static void main(String[] args) {
        String[] stocks = { "SKALA 에듀", "SKALA AI", "K-테크", "SKALA 테크", "N-솔루션" };

        // 정렬 전 주식 목록 출력
        System.out.println("정렬 전 주식 목록:");
        for (String stock : stocks) {
            System.out.println(stock);
        }

        // 람다 표현식을 사용하여 정렬
        java.util.Arrays.sort(stocks, (s1, s2) -> s1.compareTo(s2));

        // 정렬된 주식 목록 출력
        System.out.println("\n정렬된 주식 목록:");
        for (String stock : stocks) {
            System.out.println(stock);
        }
    }
}
