package exceptions;

public class SemanticException extends RuntimeException {

    public SemanticException(String message) {
        super(message);
    }

    public SemanticException(int row, int col, String message) {
        super("Semantic exception at [row="+row+", col="+col+"]: " + message + ";");
    }
}
