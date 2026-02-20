package ch5_util;

import java.util.HashMap;

public class HashMapExample {

    static class Player {

        private final String name;
        private final HashMap<String, Integer> ownedStocks = new HashMap<>();

        public Player(String name) {
            this.name = name;
        }

        // 주식 매수
        public void buyStock(String stockName, int quantity) {
            int current = ownedStocks.getOrDefault(stockName, 0);
            ownedStocks.put(stockName, current + quantity);
        }

        public void printPortfolio() {
            System.out.println(name + "님의 보유 주식:");
            for (String stock : ownedStocks.keySet()) {
                int qty = ownedStocks.get(stock);
                System.out.println("- " + stock + ": " + qty + "주");
            }
        }
    }

    public static void main(String[] args) {
        Player player = new Player("홍길동");

        // 주식 매수
        player.buyStock("SKALA 에듀", 10);
        player.buyStock("SKALA AI", 5);
        player.buyStock("SKALA 에듀", 15); // 추가 매수

        // 포트폴리오 출력
        player.printPortfolio();
    }
}
