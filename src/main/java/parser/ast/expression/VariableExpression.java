package parser.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.DataType;
import parser.ast.Variable;

@AllArgsConstructor
public class VariableExpression implements Expression {

    @Getter
    private Variable variable;

    @Override
    public DataType returnType() {
        return variable.getType();
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
        return toString();
    }

    @Override
    public String toString() {
        return variable.getName();
    }
}
