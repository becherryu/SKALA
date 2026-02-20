package ch7_lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PredicateExample {

    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        // 문자열이 "A"로 시작하는지 테스트하는 Predicate 생성
        Predicate<String> startsWithA = (s) -> s.startsWith("A");

        // Predicate를 사용하여 리스트 필터링
        List<String> namesStartingWithA = names.stream()
                .filter(startsWithA)
                .collect(Collectors.toList());

        System.out.println(namesStartingWithA); // 출력: [Alice]

        // filter 메서드에 직접 람다를 사용할 수도 있음
        List<String> namesWithLength5 = names.stream()
                .filter(s -> s.length() == 5)
                .collect(Collectors.toList());

        System.out.println(namesWithLength5); // 출력: [Alice, David]
    }
}
