package ch4_core;

public class ShutdownHookExample {

    public static void main(String[] args) {
        // JVM 종료 훅 등록
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("JVM 종료 감지 → cleanup 실행");
            cleanup();
        }));

        System.out.println("프로그램 실행 중... 종료하려면 Ctrl+C 또는 kill 신호를 보내세요.");

        while (true) {
            try {
                Thread.sleep(5000);
                System.out.println("작업 실행 중...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void cleanup() {
        System.out.println("리소스를 정리하는 중...");
    }

}
