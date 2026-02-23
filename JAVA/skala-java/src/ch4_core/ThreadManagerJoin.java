package ch4_core;

public class ThreadManagerJoin {

    // 메인 함수에서 새로운 Thread를 생성하여 실행하고 join() 호출 후 "메인 함수를 종료합니다" 출력 후 exit(0)
    // Thread는 내부적으로 1초간 sleep후 "새로운 스레드 작업을 시작합니다." 출력
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("새로운 스레드 작업을 시작합니다.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread was interrupted: " + e.getMessage());
            }
        });

        t.start();
        try {
            t.join(); // 메인 스레드가 t 스레드가 종료될 때까지 기다립니다.
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread was interrupted: " + e.getMessage());
        }
        System.out.println("메인 함수를 종료합니다.");
        System.exit(0);
    }
}