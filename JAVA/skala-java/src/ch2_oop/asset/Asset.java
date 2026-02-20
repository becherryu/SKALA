package ch2_oop.asset;

abstract class Asset {

    protected String name;
    protected double price;

    public Asset(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public abstract void printInfo();
}
