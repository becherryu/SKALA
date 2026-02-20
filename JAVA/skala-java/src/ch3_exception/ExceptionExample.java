package ch3_exception;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExceptionExample {

    // BufferedReader 사용하여 data.txt 파일의 내용을 읽어서 출력하는 메서드
    public static void main(String[] args) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("data.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("예외가 발생했습니다: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    System.out.println("리소스를 정리합니다.");
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println("리소스 정리 중 예외가 발생했습니다: " + e.getMessage());
            }
            System.out.println("파일 읽기 작업이 완료되었습니다.");
        }
    }
}
