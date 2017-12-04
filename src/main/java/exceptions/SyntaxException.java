package exceptions;

public class SyntaxException extends RuntimeException {
    public SyntaxException(String message) {
        super(message);
    }

    public SyntaxException(int row, int col, String message) {
        super("Syntax exception at [row="+row+", col="+col+"]: " + message + ";");
    }
}
