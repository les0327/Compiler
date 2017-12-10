package parser;


public enum TokenType {
    TRUE,
    FALSE,
    NUMBER,
    VAR,

    // keyword
    IF,
    ELSE,
    BREAK,
    SWITCH,
    CASE,
    DEFAULT,

    // types
    INT,
    BOOL,

    COLON, // :
    SEMI_COLON, // ;

    PLUS, // +
    MINUS, // -
    MUL, // *
    DIV, // /
    PERCENT,// %

    ASSIGN, // =
    EQUALS, // ==
    NOT, // !
    NOT_EQUALS, // !=
    LESS_EQUALS, // <=
    LESS, // <
    MORE, // >
    MORE_EQUALS, // >=

    LEFT_SHIFT, // <<
    RIGHT_SHIFT, // >>

    XOR, // ^
    OR, // |
    OR_LOGICAL, // ||
    AND, // &
    AND_LOGICAL, // &&
    QUESTION, // ?

    LEFT_ROUND_BRACKET, // (
    RIGHT_ROUND_BRACKET, // )
    LEFT_BRACE, // {
    RIGHT_BRACE, // }

    EOF
}
