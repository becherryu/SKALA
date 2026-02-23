package ch8_chaining;

public class Stock {

    private String name;
    private int price;

    public Stock() {
    }

    public Stock(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Stock setName(String name) {
        this.name = name;
        return this;
    }

    public Stock setPrice(int price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
