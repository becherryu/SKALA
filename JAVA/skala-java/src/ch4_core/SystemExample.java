package ch4_core;

public class SystemExample {

    public static void main(String[] args) {
        System.out.println("스칼라에 오신 것을 환영합니다.");

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            // do nothing
        }
        long end = System.currentTimeMillis();

        System.out.println("걸린 시간: " + (end - start) + "ms");

        // System.exit(0); // 프로그램 종료
    }
}
