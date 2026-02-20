package ch7_lambda;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PersonComparator {

    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
                new Person("Alice", 30),
                new Person("Bob", 25),
                new Person("Charlie", 35)
        );

        // 나이순으로 정렬하는 Comparator 생성
        Comparator<Person> byAge = (p1, p2) -> Integer.compare(p1.getAge(), p2.getAge());

        // Comparator를 사용하여 리스트 정렬
        Collections.sort(people, byAge);

        System.out.println(people); // 출력: [Bob (25), Alice (30), Charlie (35)]

        // 더 현대적인 방식: List.sort와 Comparator의 편의 메서드 사용
        people.sort(Comparator.comparing(Person::getAge).reversed()); // 나이 역순으로 정렬

        System.out.println(people); // 출력: [Charlie (35), Alice (30), Bob (25)]
    }

}
