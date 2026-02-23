/**
 * 주식의 이름, 가격, 수량을 저장하는 데이터 클래스입니다.
 */
public class Stock {
    private String name;
    private int price;
    private int quantity;

    public Stock(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    // Setters

    @Override
    public String toString() {
        return "종목: " + name + ", 현재가: " + price + ", 보유수량: " + quantity;
    }
}