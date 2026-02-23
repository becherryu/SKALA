/**
 * 주식의 이름, 가격, 수량을 저장하는 데이터 클래스입니다.
 */
public class Stock {
    private final String name;
    private int price;
    private int quantity;

    public Stock(String name, int price, int quantity) {
        validateName(name);
        validateNonNegative("price", price);
        validateNonNegative("quantity", quantity);
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
    public Stock setPrice(int price) {
        validateNonNegative("price", price);
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

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름을 작성해주세요.");
        }
    }

    private static void validateNonNegative(String fieldName, int value) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + "는 0 이상이어야 합니다.");
        }
    }
}
