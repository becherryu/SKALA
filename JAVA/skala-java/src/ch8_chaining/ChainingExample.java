package ch8_chaining;

public class ChainingExample {

    public static void main(String[] args) {
        String result = "  hello java  "
                .trim()
                .toUpperCase()
                .replace("JAVA", "SKALA");

        System.out.println(result); // HELLO SKALA
    }
}
