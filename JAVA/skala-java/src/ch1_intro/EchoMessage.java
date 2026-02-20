package ch1_intro;

import java.util.Scanner;

public class EchoMessage {
    // 사용자가 입력한 문자열을 그대로 출력하는 프로그램 작성
    // 콘솔에서 입력을 받고, 입력받은 문자열을 다시 콘솔에 출력
    // "quit" 또는 "exit"을 입력할 때까지 계속해서 입력을 받도록 구현

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in) // 입력 도구 생성
        ) {
            System.out.println("메시지를 입력하세요 (종료하려면 'quit' 또는 'exit' 입력):");
            String input;
            while ((input = scanner.nextLine()) != null) {
                if (input.equals("quit") || input.equals("exit")) {
                    System.out.println("프로그램을 종료합니다.");
                    break; // 루프 종료
                }
                System.out.println("입력한 메시지: " + input); // 입력받은 메시지 출력
            }
            // Scanner 자원 해제
        }
    }
}
