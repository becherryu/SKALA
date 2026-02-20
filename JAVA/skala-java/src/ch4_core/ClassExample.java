package ch4_core;

public class ClassExample {

    public static void main(String[] args) {
        String skala = "스칼라";
        Class<?> clazz = skala.getClass();

        System.out.println("클래스 이름: " + clazz.getName());       // java.lang.String
        System.out.println("슈퍼 클래스: " + clazz.getSuperclass()); // class java.lang.Object
    }

}
