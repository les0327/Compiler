package parser.ast.expression;

import lombok.AllArgsConstructor;
import parser.ast.DataType;
import parser.ast.Variable;

@AllArgsConstructor
public class VariableExpression implements Expression {

    public final Variable variable;

    @Override
    public DataType returnType() {
        return null;
    }

    @Override
    public String toAsm() {
        return null;
    }
}
