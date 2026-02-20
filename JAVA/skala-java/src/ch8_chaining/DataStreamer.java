package ch8_chaining;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataStreamer {

    public static void main(String[] args) {
        try {
            Files.lines(Paths.get("src/chaining/data.txt"))
                    .filter(line -> line.contains("SKALA"))
                    .forEach(System.out::println);
        } catch (IOException ex) {
            System.getLogger(DataStreamer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }
}
