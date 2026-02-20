package ch4_core;

public class ObjectStockMain {

    // ObjectStock의 name이 같으면 같은 주식으로 간주되는지 확인하는 코드
    public static void main(String[] args) {
        ObjecStock stock1 = new ObjecStock("삼성전자", 72000);
        ObjecStock stock2 = new ObjecStock("삼성전자", 180000);

        System.out.println(stock1 == stock2);
    }
}
