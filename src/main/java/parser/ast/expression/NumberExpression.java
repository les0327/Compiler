package parser.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.DataType;
import parser.ast.NumberValue;

@AllArgsConstructor
public class NumberExpression implements Expression {

    @Getter
    private NumberValue value;

    @Override
    public DataType returnType() {
        return DataType.Int;
    }

    @Override
    public boolean canOptimize() {
        return true;
    }

    @Override
    public Expression optimize() {
        return this;
    }

    @Override
    public String toAsm() {
        return toString();
    }

    @Override
    public String toString() {
        return value.getValue().toString();
    }
}
