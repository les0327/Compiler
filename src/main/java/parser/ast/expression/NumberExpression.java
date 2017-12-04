package parser.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.DataType;

@AllArgsConstructor
public class NumberExpression implements Expression {

    @Getter
    private Number value;

    @Override
    public DataType returnType() {
        return null;
    }

    @Override
    public String toAsm() {
        return toString();
    }
}
