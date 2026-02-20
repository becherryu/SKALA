package ch1_intro;

public class HelloSkala {
    // "스칼라에 오신 것을 환영합니다." 출력
    public static void main(String[] args) {
        // System.out.println("스칼라에 오신 것을 환영합니다.");

        // 방법 1: try-catch 블록을 사용하여 InterruptedException을 처리
        // 방법 2: main 메서드에 throws InterruptedException 선언하여 예외를 호출자에게 전달
        // 스칼라에 + 1초 휴식 + 오신 것을 환영합니다 출력
        System.out.print("스칼라에");
        try {
            Thread.sleep(5000); // 1초 휴식
        } catch (InterruptedException e) {
            // e.printStackTrace(); <- 별로 좋지 않은 에러 처리
            System.out.println("대기 중 오류 발생 : " + e.getMessage());
        }
        System.out.println(" 오신 것을 환영합니다.");
    }

}
