package ch6_generic.bound;

import java.util.ArrayList;
import java.util.List;

public class NumberBoxExample {

    public static void main(String[] args) {
        // Upper Bound (<T extends Type>) 예제
        System.out.println("=== Upper Bound 예제 ===");
        NumberBox<Integer> intBox = new NumberBox<>();
        intBox.setValue(10);
        intBox.printDouble();

        NumberBox<Double> doubleBox = new NumberBox<>();
        doubleBox.setValue(5.5);
        doubleBox.printDouble();

        // Lower Bound (? super T) 예제
        // Integer의 부모 타입(Integer, Number, Object)을 받을 수 있음
        System.out.println("=== Lower Bound 예제 ===");

        // Integer의 부모인 Number 리스트
        List<Number> numberList = new ArrayList<>();
        addIntegers(numberList);
        System.out.println("Number 리스트: " + numberList);

        // Integer 자신의 리스트도 가능
        List<Integer> integerList = new ArrayList<>();
        addIntegers(integerList);
        System.out.println("Integer 리스트: " + integerList);

        // Integer의 부모인 Object 리스트
        List<Object> objectList = new ArrayList<>();
        addIntegers(objectList);
        System.out.println("Object 리스트: " + objectList);
    }

    // Lower Bound: ? super Integer
    // Integer와 그 부모 타입(Number, Object)만 허용
    // 용도: 데이터를 "추가"할 때 사용 (Producer Extends, Consumer Super)
    public static void addIntegers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
        // Integer를 안전하게 추가할 수 있음
    }
}
