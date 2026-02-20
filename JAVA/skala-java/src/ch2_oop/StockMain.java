package ch2_oop;

public class StockMain {

    public static void main(String[] args) {
        Stock s1 = new Stock("SKALA");
        Stock s2 = new Stock("SKALA");

        System.out.println(s1);   // toString() 호출
        System.out.println(s1.equals(s2));   // true
    }

}
