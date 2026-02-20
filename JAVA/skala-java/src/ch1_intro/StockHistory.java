package ch1_intro;

public class StockHistory {
    // StringBuilder로 문자열을 동적으로 조립
    // 가격은 천 단위 콤마 표시 (DecimalFormat), 등락률(%)은 소수점 2자리까지 표시
    public static void main(String[] args) {
        String[] stocks = { "SKALA", "EDUAI", "KQTECH" };
        int[] todayPrices = { 12300, 9850, 23000 };
        int[] yesterdayPrices = { 12000, 10200, 22000 };

        System.out.println("종목명           전일가       현재가       등락률");
        System.out.println("=================================================");

        for (int i = 0; i < stocks.length; i++) {
            double changeRate = yesterdayPrices[i] == 0 ? 0
                    : ((double) (todayPrices[i] - yesterdayPrices[i]) / yesterdayPrices[i]) * 100;
            System.out.printf("%-13s%,10d%,13d%+11.2f%%%n",
                    stocks[i], yesterdayPrices[i], todayPrices[i], changeRate);
        }
    }
}
