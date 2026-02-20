package ch6_generic.box;

public class StockBoxExample {

    public static void main(String[] args) {
        // int를 담고 꺼내기 (Wrapper 클래스 사용)
        Box<Integer> intBox = new Box<>();
        intBox.setItem(100);
        System.out.println("정수: " + intBox.getItem());

        // String을 담고 꺼내기
        Box<String> strBox = new Box<>();
        strBox.setItem("SKALA AI");
        System.out.println("문자열: " + strBox.getItem());

        // 사용자 정의 Stock 객체
        Stock stock = new Stock("SKALA-EDU", 18000.0);
        Box<Stock> stockBox = new Box<>();
        stockBox.setItem(stock);
        System.out.println("주식명: " + stockBox.getItem().getName());
    }

}
