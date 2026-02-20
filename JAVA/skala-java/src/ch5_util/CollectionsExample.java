package ch5_util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionsExample {

    public static void main(String[] args) {
        List<Integer> prices = new ArrayList<>();
        prices.add(18000);
        prices.add(9500);
        prices.add(12000);

        // 오름차순 정렬
        Collections.sort(prices);
        System.out.println("오름차순: " + prices);  // [9500, 12000, 18000]

        // 내림차순 정렬
        Collections.reverse(prices);
        System.out.println("내림차순: " + prices);  // [18000, 12000, 9500]

        // Stock 객체 리스트 정렬후 이름 오름차순으로 출력
        List<Stock> stockList = new ArrayList<>();
        stockList.add(new Stock("SKALA 에듀", 12000));
        stockList.add(new Stock("SKALA AI", 18000));
        stockList.add(new Stock("SKALA 테크", 9500));

        // Collections.sort() 사용하여 이름 기준 오름차순 정렬
    }
}
