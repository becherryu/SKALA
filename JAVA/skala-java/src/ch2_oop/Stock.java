package ch2_oop;

public class Stock {

    String name;

    public Stock(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Stock[name=" + name + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Stock other) {
            return this.name.equals(other.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
