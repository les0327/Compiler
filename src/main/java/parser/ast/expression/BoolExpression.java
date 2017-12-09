package parser.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import parser.ast.BoolValue;
import parser.ast.DataType;

@AllArgsConstructor
public class BoolExpression implements Expression {

    @Getter
    @Setter
    private BoolValue value;

    @Override
    public DataType returnType() {
        return DataType.Bool;
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
        return value.isValue() ? "1" : "0";
    }

    @Override
    public String toString() {
        return value.isValue() + "";
    }
}
