package ch6_generic.wildcard;

import java.util.ArrayList;
import java.util.List;

public class WildcardExample {

    public static void main(String[] args) {

        // List<Animal>에는 Animal과 그 자손들(Dog, Cat) 모두 추가 가능
        List<Animal> animals = new ArrayList<>();
        animals.add(new Dog());
        animals.add(new Cat());

        for (Animal a : animals) {
            a.speak();  // OK
        }

        // 와일드카드 <? extends Animal> 사용
        List<? extends Animal> animalList = new ArrayList<Dog>();
        // animalList.add(new Dog());     // 오류
        // animalList.add(new Animal());  // 오류

        // Animal a = animalList.get(0);  // OK
        // a.speak();
        // 와일드카드 <? super Dog> 사용
        List<? super Dog> dogList = new ArrayList<Animal>();
        dogList.add(new Dog());      // OK
        // dogList.add(new Animal());   // 오류

        Object obj = dogList.get(0); // 읽을 수 있지만 Object로 나옴
        System.out.println("읽은 객체 타입: " + obj.getClass().getSimpleName());
        ((Dog) obj).speak();

    }
}
