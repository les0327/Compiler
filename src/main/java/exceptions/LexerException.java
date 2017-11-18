package exceptions;


public final class LexerException extends RuntimeException {

    public LexerException(String message) {
        super(message);
    }
    
    public LexerException(int row, int col, String message) {
        super("Lexer exception at [row="+row+", col="+col+"]: " + message + ";");
    }
}