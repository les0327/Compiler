package parser.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.DataType;
import parser.ast.Pointer;

@AllArgsConstructor
public class PointerExpression implements Expression {

    @Getter private Pointer pointer;

    @Override
    public DataType returnType() {
        return pointer.getType();
    }

    @Override
    public boolean canOptimize() {
        return false;
    }

    @Override
    public Expression optimize() {
        return this;
    }

    @Override
    public String toAsm() {
        return "[" + pointer.getName() + "]";
    }

    @Override
    public String toString() {
        return "*" + pointer.getName();
    }
}
