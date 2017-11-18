package parser.ast.variables;

public interface IVariable<T> {
    String getName();
    T getValue();
    int getAddress();
    void setValue(T value);
    void setAddress(int address);
}
