package parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Getter
    private TokenType type;
    @Getter
    private String text;
    @Getter
    private int row;
    @Getter
    private int col;

    public Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    public String getPosition() {
        return "(row=" + row + ", col=" + col + ")";
    }

    @Override
    public String toString() {
        return text + " -> {type=" + type.toString() + ", row=" + row + ", col=" + col + "}";
    }

}
