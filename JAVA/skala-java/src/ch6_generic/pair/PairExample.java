package ch6_generic.pair;

public class PairExample {

    public static void main(String[] args) {
        Pair<String, Integer> stockPair = new Pair<>("SKALA-AI", 10000);
        System.out.println("종목: " + stockPair.getKey() + ", 가격: " + stockPair.getValue());
    }
}
