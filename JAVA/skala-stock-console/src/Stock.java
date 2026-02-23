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
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public Stock setName(String name) {
        this.name = name;
        return this;
    }

    public Stock setPrice(int price) {
        this.price = price;
        return this;
    }

    public Stock setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public String toString() {
        return "종목: " + name + ", 현재가: " + price + ", 보유수량: " + quantity;
    }
}