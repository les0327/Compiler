package parser.ast;

public interface Value {

    String getName();

    DataType getType();

    boolean isAssigned();

    void setAssigned(boolean value);

    boolean isPointer();
}