import exceptions.LexerException;
import parser.Lexer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {
        String input = new String(Files.readAllBytes(Paths.get(Main.class.getResource("input.c").toURI()))).trim();
//        System.out.println(input);
        try {
            Lexer.tokenize(input).forEach(System.out::println);
        } catch (LexerException e) {
            System.out.println(e.getMessage());;
        }
    }
}
