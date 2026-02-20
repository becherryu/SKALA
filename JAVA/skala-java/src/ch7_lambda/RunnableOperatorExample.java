package ch7_lambda;

public class RunnableOperatorExample {

    public static void main(String[] args) {
        // 구현 클래스 사용
        Runnable myRunnable = new RunnableOperator();
        Thread thread1 = new Thread(myRunnable);
        thread1.start();

        // 람다 표현식 사용 (요즘 더 일반적임)
        Runnable lambdaRunnable = () -> System.out.println("람다를 사용한 코드 역시 별도의 스레드에서 실행됩니다.");

        // 화살표 함수는 다음과 같이 블록으로도 작성 가능
        /*
        Runnable lambdaRunnable = () -> {
            System.out.println("람다를 사용한 코드 역시 별도의 스레드에서 실행됩니다.");
        };
         */
        Thread thread2 = new Thread(lambdaRunnable);
        thread2.start();
    }

}
