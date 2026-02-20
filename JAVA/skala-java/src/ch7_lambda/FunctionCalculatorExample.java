package ch7_lambda;

public class FunctionCalculatorExample {

    public static void main(String[] args) {
        FunctionCalculator add = (a, b) -> a + b;
        FunctionCalculator multiply = (a, b) -> a * b;

        System.out.println("덧셈: " + add.operate(10, 5));
        System.out.println("곱셈: " + multiply.operate(10, 5));
    }

}
