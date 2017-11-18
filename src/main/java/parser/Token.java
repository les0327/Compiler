package parser;

import lombok.Getter;

public final class Token {

    @Getter private final TokenType type;
    @Getter private final String text;
    @Getter private final int row;
    @Getter private final int col;

    public Token(TokenType type, String text, int row, int col) {
        this.type = type;
        this.text = text;
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return text + " -> {type=" + type.toString()+ ", row=" + row + ", col=" + col + "}";
    }
}
