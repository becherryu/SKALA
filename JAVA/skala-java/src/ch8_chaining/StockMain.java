package ch8_chaining;

public class StockMain {
    public static void main(String[] args) {
        Stock stock = new Stock();

        stock.setName("SKALA AI")
                .setPrice(15000);

        System.out.println(stock);
    }
}
