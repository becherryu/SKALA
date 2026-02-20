package ch7_lambda;

public class RunnableOperator implements Runnable {

    @Override
    public void run() {
        System.out.println("이 코드는 별도의 스레드에서 실행됩니다.");
    }

}
