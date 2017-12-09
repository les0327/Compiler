package parser.ast.expression;

import parser.ast.DataType;

public interface Expression {
    DataType returnType();

    boolean canOptimize();

    Expression optimize();

    String toAsm();

    default boolean isTerminal() {
        return this instanceof AddressExpression || this instanceof BoolExpression || this instanceof NumberExpression || this instanceof PointerExpression || this instanceof VariableExpression;
    }

}
