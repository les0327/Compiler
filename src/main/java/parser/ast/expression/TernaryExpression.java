package parser.ast.expression;

import lombok.AllArgsConstructor;
import parser.ast.DataType;

@AllArgsConstructor
public final class TernaryExpression implements Expression {

    public final Expression condition;
    public final Expression trueExpr, falseExpr;

    @Override
    public DataType returnType() {
        return null; // TODO: 04-Dec-17
    }

    @Override
    public String toString() {
        return String.format("%s ? %s : %s", condition, trueExpr, falseExpr);
    }

    @Override
    public String toAsm() {
        return null;
    }
}
