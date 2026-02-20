package ch2_oop;

public class StockUtilsMain {

    // StockUtils 클래스의 메서드들을 테스트하는 메인 클래스
    public static void main(String[] args) {
        StockUtils stockUtils = new StockUtils();

        stockUtils.printWelcomeMessage();
        stockUtils.printStockPrice("SKALA", 10000);
        System.out.println(stockUtils.getMarketStatus());
        System.out.println(stockUtils.calculatePriceChange(9500, 10000));
        stockUtils.printStockInfo("SKALA");
    }
}
