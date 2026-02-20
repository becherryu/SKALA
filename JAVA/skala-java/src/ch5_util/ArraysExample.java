package ch5_util;

import java.util.Arrays;

public class ArraysExample {

    public static void main(String[] args) {
        int[] prices = {18000, 9500, 12000};

        Arrays.sort(prices);  // 오름차순 정렬
        System.out.println("정렬된 주식 가격: " + Arrays.toString(prices));    // 정렬된 주식 가격: [9500, 12000, 18000]
    }

}
