package parser;


public enum TokenType {

    NUMBER,
    VAR,

    // keyword
    IF,
    ELSE,
    BREAK,
    SWITCH,
    CASE,
    DEFAULT,
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

    PLUS_EQUALS, // +=
    MINUS_EQUALS, // -=
    MUL_EQUALS, // *=
    DIV_EQUALS, // /=
    PERCENT_EQUALS, // %=
    END_EQUALS, // &=
    XOR_EQUALS, // ^=
    OR_EQUALS, // |=

    INCREMENT, // ++
    DECREMENT, // --

    LEFT_SHIFT, // <<
    RIGHT_SHIFT, // >>
    
    TILDE, // ~
    XOR, // ^
    OR, // |
    OR_LOGICAL, // ||
    AND, // &
    AND_LOGICAL, // &&

    LEFT_ROUND_BRACKET, // (
    RIGHT_ROUND_BRACKET, // )
    LEFT_BRACE, // {
    RIGHT_BRACE, // }
    
    EOF
}
