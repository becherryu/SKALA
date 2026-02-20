package ch4_core;

public class RuntimeExample {

    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
        System.out.println("메모리 사용 전: " + rt.freeMemory());

        int[] bigArray = new int[1000000];
        System.out.println("메모리 사용 후: " + rt.freeMemory());

        bigArray = null;
        System.gc(); // 가비지 컬렉션 요청

        System.out.println("GC 후: " + rt.freeMemory());
    }

}
