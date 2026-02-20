package ch5_util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesExample {

    public static void main(String[] args) {
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream("src/util/config.properties"); InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            props.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(props.getProperty("welcome.message")); // 출력: 스칼라에 오신 것을 환영합니다.    
    }
}
