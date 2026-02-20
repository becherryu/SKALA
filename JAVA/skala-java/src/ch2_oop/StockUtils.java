package ch2_oop;

public class StockUtils {

    // 1. 반환값도 없고, 매개변수도 없음
    public void printWelcomeMessage() {
        System.out.println("주식 정보 시스템에 오신 것을 환영합니다.");
    }

    // 2. 매개변수만 있고, 반환값 없음
    public void printStockPrice(String stockName, double price) {
        System.out.println(stockName + "의 현재 가격은 " + price + "원입니다.");
    }

    // 3. 반환값만 있고, 매개변수 없음
    public String getMarketStatus() {
        return "장 마감";
    }

    // 4. 매개변수도 있고, 반환값도 있음
    public double calculatePriceChange(double yesterday, double today) {
        System.out.println("가격 변동 계산 중...");
        return today - yesterday;
    }

    // 5. 오버로딩(Overloading): 이름은 같지만 매개변수가 다름
    public void printStockInfo(String stockName) {
        System.out.println("[" + stockName + "] 종목 정보 조회");
    }
}

