package parser.ast;

public interface Value {

    String getName();
    DataType getType();
    boolean isAssigned();
    boolean isPointer();
    void setAssigned(boolean value);
}