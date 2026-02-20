package ch3_exception;

public class UserExceptionMain {
    // UserException을 발생시키는 예제
    public static void main(String[] args) {
        int userInput = -1; // 예시로 잘못된 입력값 설정
        try {
            validateUserInput(userInput);
            System.out.println("입력이 유효합니다: " + userInput);
        } catch (UserException e) {
            System.err.println("사용자 정의 예외가 발생했습니다: " + e.getMessage());
        }
    }

    public static void validateUserInput(int input) throws UserException {
        if (input < 0) {
            throw new UserException("0 이상의 값을 입력해주세요.");
        }
    }
}
