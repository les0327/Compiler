import parser.Lexer;
import parser.Parser;
import parser.Token;
import parser.ast.statement.Statement;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String input = new String(Files.readAllBytes(Paths.get(Main.class.getResource("input.c").toURI()))).trim();
        List<Token> tokens = Lexer.tokenize(input);
        tokens.forEach(System.out::println);
        Statement s = Parser.parse(tokens);
    }
}
