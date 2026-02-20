package ch4_core;

public class ObjecStock {

    String name;
    double price;

    public ObjecStock(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // ObjectStock의 name이 같으면 같은 주식으로 간주되는지 확인하는 코드
    // equals와 hashCode를 오버라이드하여 name이 같으면 같은 주식으로 간주되도록 구현
}
