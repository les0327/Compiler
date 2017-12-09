import exceptions.SemanticException;
import parser.Lexer;
import parser.Parser;
import parser.Token;
import parser.ast.statement.Statement;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            String input = new String(Files.readAllBytes(Paths.get(args[0]))).trim();
            System.out.println("Read file        -> ok");
            List<Token> tokens = Lexer.tokenize(input);
            System.out.println("Lexical analyse  -> ok");
            Statement s = Parser.parse(tokens);
            System.out.println("Syntax analyse   -> ok");
            s.semanticAnalyse();
//        System.out.println(s);
            System.out.println("Semantic analyse -> ok");
            s.optimize();
//        System.out.println(s);
            System.out.println("Optimization     -> ok");
            Path output = Paths.get(args[1]);
            Files.deleteIfExists(output);
            Files.createFile(output);
            String out = ".586\n" +
                    ".model flat, c\n" +
                    ".code\n" +
                    "main proc\n";

            String[] lines = s.toAsm().split("\n");
            for (int i = 0; i < lines.length; i++)
                if (!lines[i].startsWith("@"))
                    lines[i] = "\t" + lines[i];

            out += String.join("\n", lines);

            out += "\nret\n" +
                    "main endp\n";
            Files.write(Paths.get(args[1]), out.getBytes());
            System.out.println("Generate code    -> ok");
        } catch (ClassCastException e) {
            throw new SemanticException("invalid convertion from int* to int");
        }
    }
}
