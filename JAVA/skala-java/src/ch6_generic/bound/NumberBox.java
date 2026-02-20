package ch6_generic.bound;

class NumberBox<T extends Number> {

    private T value;

    public void printDouble() {
        System.out.println(value.doubleValue());
    }

    public void setValue(T value) {
        this.value = value;
    }

}
