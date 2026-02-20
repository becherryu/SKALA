package ch1_intro;

public class NumberBox {
    // 기본형 int a와 b는 100, 1000으로 초기화
    // 래퍼 클래스 Integer A와 B도 100,1000 으로 초기화 되었을 때 비교(==, equals()) 결과 출력
    public static void main(String[] args) {
        int a = 1000;
        int b = 1000;

        Integer A = 1000;
        Integer B = 1000;

        // 기본형 비교 (값 비교)
        System.out.println("a == b : " + (a == b)); // true

        // 래퍼 클래스 비교 (참조 비교)
        /*
         * false (Integer 객체는 1000이므로 캐싱되지 않음)
         * Integer 객체는 -128에서 127까지의 값에 대해서는 캐싱 됨(동일 개체 참조)
         * 같은 주소 값이냐? 라고 묻는 것과 같음
         */
        System.out.println("A == B : " + (A == B));

        // 기본형과 래퍼 클래스 비교 (오토 언박싱)
        System.out.println("A == a : " + (A == a)); // true
        System.out.println("B == b : " + (B == b)); // true

        // 래퍼 클래스 equals() 메서드 비교 (값 비교)
        System.out.println("A.equals(B) : " + A.equals(B)); // true
    }
}
