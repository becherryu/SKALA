package ch5_util;

public class Stock {

    final String name;
    final int price;

    public Stock(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public void printInfo() {
        System.out.println("주식 이름: " + name + ", 가격: " + price);
    }
}
